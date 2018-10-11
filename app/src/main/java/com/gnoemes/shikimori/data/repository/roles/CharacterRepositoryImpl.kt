package com.gnoemes.shikimori.data.repository.roles

import com.gnoemes.shikimori.data.network.RolesApi
import com.gnoemes.shikimori.data.repository.roles.converter.CharacterDetailsResponseConverter
import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails
import io.reactivex.Single
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
        private val api: RolesApi,
        private val converter: CharacterDetailsResponseConverter
) : CharacterRepository {

    override fun getDetails(id: Long): Single<CharacterDetails> = api.getCharacterDetails(id).map(converter::convertResponsse)

}