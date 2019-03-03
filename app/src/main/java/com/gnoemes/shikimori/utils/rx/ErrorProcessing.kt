package com.gnoemes.shikimori.utils.rx

import com.crashlytics.android.Crashlytics
import com.gnoemes.shikimori.entity.app.domain.exceptions.*
import com.google.gson.JsonSyntaxException
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.net.ssl.SSLHandshakeException

class ErrorProcessing<T> {

    fun getObservableErrors(throwable: Throwable): Observable<T> {
        return Observable.error { throwProcessException(throwable) }
    }

    fun getSingleErrors(throwable: Throwable): Single<T> {
        return Single.error { throwProcessException(throwable) }
    }

    fun getCompletableErrors(throwable: Throwable): Completable {
        return Completable.error { throwProcessException(throwable) }
    }

    @Throws(TitleException::class, NetworkException::class, ServiceCodeException::class, ContentException::class)
    internal fun throwProcessException(throwable: Throwable): Throwable {

        Crashlytics.logException(throwable)

        if (throwable is UnknownHostException) {
            throw NetworkException("Internet error")
        }

        if (throwable is ConnectException) {
            throw NetworkException("Internet error")
        }

        if (throwable is SSLHandshakeException) {
            throw NetworkException("SSLHandshake error")
        }

        if (throwable is SocketTimeoutException) {
            throw NetworkException("Connection timeout")
        }

        if (throwable is JsonSyntaxException) {
            throw TitleException("", "Json syntax error")
        }

        if (throwable is HttpException) {
            throw ServiceCodeException(throwable.code())
        }

        if (throwable is NoSuchElementException) {
            throw ContentException("")
        }

        return throwable as? BaseException ?: TitleException("", throwable.message)
    }
}