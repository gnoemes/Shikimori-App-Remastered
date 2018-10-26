package com.gnoemes.shikimori.data.repository.roles

import com.gnoemes.shikimori.data.network.RolesApi
import com.gnoemes.shikimori.data.repository.roles.converter.PersonDetailsResponseConverter
import com.gnoemes.shikimori.entity.roles.domain.PersonDetails
import io.reactivex.Single
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
        private val api: RolesApi,
        private val responseConverter: PersonDetailsResponseConverter
) : PersonRepository {

    override fun getDetails(id: Long): Single<PersonDetails> = api.getPersonDetails(id).map(responseConverter::convertResponse)
}