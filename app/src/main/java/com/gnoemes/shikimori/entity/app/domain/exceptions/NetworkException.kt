package com.gnoemes.shikimori.entity.app.domain.exceptions

class NetworkException(private val _message: String?) : BaseException(TAG) {

    override val message: String?
        get() = _message

    companion object {
        const val TAG = "NetworkException"
    }
}