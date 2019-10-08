package com.gnoemes.shikimori.domain.chronology

import com.gnoemes.shikimori.data.repository.anime.AnimeRepository
import com.gnoemes.shikimori.data.repository.manga.MangaRepository
import com.gnoemes.shikimori.data.repository.ranobe.RanobeRepository
import com.gnoemes.shikimori.data.repository.rates.RatesRepository
import com.gnoemes.shikimori.data.repository.search.SearchRepository
import com.gnoemes.shikimori.data.repository.user.UserRepository
import com.gnoemes.shikimori.domain.search.SearchQueryBuilder
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.chronology.ChronologyItem
import com.gnoemes.shikimori.entity.chronology.ChronologyType
import com.gnoemes.shikimori.entity.common.domain.*
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ChronologyInteratorImpl @Inject constructor(
        private val animeRepository: AnimeRepository,
        private val mangaRepository: MangaRepository,
        private val ranobeRepository: RanobeRepository,
        private val rateRepository: RatesRepository,
        private val searchRepository: SearchRepository,
        private val userRepository: UserRepository,
        private val searchQueryBuilder: SearchQueryBuilder
) : ChronologyInteractor {

    override fun getAnimes(id: Long, franchiseName: String?, type: ChronologyType): Single<List<ChronologyItem>> =
            animeRepository.getFranchise(id)
                    .searchFranchiseItemsAndMergeWithRates(id, franchiseName, Type.ANIME, type)
                    .applyErrorHandlerAndSchedulers()

    override fun getMangas(id: Long, franchiseName: String?, type: ChronologyType): Single<List<ChronologyItem>> =
            mangaRepository.getFranchise(id)
                    .searchFranchiseItemsAndMergeWithRates(id, franchiseName, Type.MANGA, type)
                    .applyErrorHandlerAndSchedulers()

    override fun getRanobes(id: Long, franchiseName: String?, type: ChronologyType): Single<List<ChronologyItem>> =
            ranobeRepository.getFranchise(id)
                    .searchFranchiseItemsAndMergeWithRates(id, franchiseName, Type.RANOBE, type)
                    .applyErrorHandlerAndSchedulers()

    private fun Single<Franchise>.searchFranchiseItemsAndMergeWithRates(id: Long, franchiseName: String?, type: Type, chronologyType: ChronologyType) =
            this.flatMap { franchise ->
                (if (franchiseName.isNullOrBlank()) searchQueryBuilder.createQueryFromIds(franchise.nodes.map { node -> node.id }.toMutableList())
                else searchQueryBuilder.createQueryFromFranchise(franchiseName))
                        .flatMap { query ->
                            Single.zip(
                                    searchRepository.getList(type, query),
                                    getRates(type),
                                    converter.invoke(id, franchise, chronologyType)
                            )
                        }
            }

    private fun getRates(type: Type): Single<List<UserRate>> =
            if (userRepository.getUserStatus() == UserStatus.AUTHORIZED) userRepository.getMyUserId()
                    .flatMap { id -> rateRepository.getUserRates(id, target = type) }
            else Single.just(emptyList())

    //TODO refactor
    private val converter = { id: Long, franchise: Franchise, type: ChronologyType ->
        BiFunction<List<LinkedContent>, List<UserRate>, List<ChronologyItem>> { linkedItems, rates ->
            val items = mutableListOf<ChronologyItem>()

            val enterNode = franchise.relations
                    .sortedByDescending { it.weight }
                    .find { it.targetId == id && it.relation in arrayOf(RelationType.SEQUEL, RelationType.PREQUEL) }

            (if (enterNode == null) franchise.relations
                    .sortedByDescending { it.weight }
                    .filter { it.sourceId == id }
                    .toMutableList()
            else {
                var current = enterNode.copy()
                val relatedNodes = franchise
                        .relations
                        .filter { it.relation == RelationType.SEQUEL }
                        .toMutableList()

                while (relatedNodes.isNotEmpty()) {
                    val next = relatedNodes.find { it.targetId == current.sourceId }
                    relatedNodes.remove(current)
                    if (next == null) break
                    current = next.copy()
                }

                when (type) {
                    ChronologyType.MAIN -> getRootBranch(franchise.relations, current)
                    ChronologyType.LINKED_DIRECTLY -> franchise.relations.filter { it.sourceId == id && it.relation !in arrayOf(RelationType.PREQUEL, RelationType.SEQUEL) }
                    ChronologyType.ALL -> franchise.nodes
                }
            })
                    //TODO refactor
                    .forEach { relation ->
                        if (relation is FranchiseRelation) {
                            val item = linkedItems.find { it.linkedId == relation.targetId }
                            val rate = rates.find { item?.linkedId == it.targetId }

                            if (item != null) {
                                items.add(ChronologyItem(
                                        item.linkedId,
                                        rate?.id,
                                        item,
                                        relation.relation,
                                        rate?.status
                                ))
                            }
                        } else if (relation is FranchiseNode) {
                            val item = linkedItems.find { it.linkedId == relation.id }
                            val rate = rates.find { item?.linkedId == it.targetId }

                            if (item != null) {
                                items.add(ChronologyItem(
                                        item.linkedId,
                                        rate?.id,
                                        item,
                                        RelationType.NONE,
                                        rate?.status
                                ))
                            }
                        }
                    }

            return@BiFunction items
                    .sortedBy {
                        when (it.item) {
                            is Anime -> it.item.dateAired?.millis
                            is Manga -> it.item.dateAired?.millis
                            else -> 0
                        }
                    }
        }
    }

    private fun getRootBranch(relations: List<FranchiseRelation>, root: FranchiseRelation): List<FranchiseRelation> {
        val rootBranch = mutableListOf<FranchiseRelation>()

        val rootSelf = relations.find { it.targetId == root.sourceId && it.relation == RelationType.PREQUEL }
        if (rootSelf != null) rootBranch.add(rootSelf)

        var current = root
        val relatedNodes = relations
                .sortedByDescending { it.weight }
                .filter { it.relation == RelationType.SEQUEL }
                .toMutableList()

        while (relatedNodes.isNotEmpty()) {
            val next = relatedNodes.find { it.sourceId == current.targetId }
            relatedNodes.remove(current)
            rootBranch.add(current)
            if (next == null) break
            current = next.copy()
        }
        return rootBranch
    }
}