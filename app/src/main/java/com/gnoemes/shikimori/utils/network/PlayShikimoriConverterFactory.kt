package com.gnoemes.shikimori.utils.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class PlayShikimoriConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        return HtmlResponseBodyConverter(retrofit.baseUrl().toString())
    }

}