package com.gnoemes.shikimori.domain.roles

import com.gnoemes.shikimori.data.repository.roles.CharacterRepository
import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class CharacterInteractorImpl @Inject constructor(
        private val repository: CharacterRepository
) : CharacterInteractor {

    override fun getDetails(id: Long): Single<CharacterDetails> = repository.getDetails(id).applyErrorHandlerAndSchedulers()
}