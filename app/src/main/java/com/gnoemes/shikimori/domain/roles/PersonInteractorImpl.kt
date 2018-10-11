package com.gnoemes.shikimori.domain.roles

import com.gnoemes.shikimori.data.repository.roles.PersonRepository
import com.gnoemes.shikimori.entity.roles.domain.PersonDetails
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class PersonInteractorImpl @Inject constructor(
        private val repository: PersonRepository
) : PersonInteractor {

    override fun getDetails(id: Long): Single<PersonDetails> = repository.getDetails(id).applyErrorHandlerAndSchedulers()
}