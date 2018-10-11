package com.gnoemes.shikimori.entity.app.domain.exceptions

class ServiceCodeException(val serviceCode: Int) : BaseException(TAG) {

    companion object {
        const val TAG = "ServiceCodeException"
    }
}