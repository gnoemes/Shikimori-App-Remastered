package com.gnoemes.shikimori.utils.network

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter

class HtmlResponseBodyConverter(
        private val baseUrl : String
) : Converter<ResponseBody, Document> {

    override fun convert(value: ResponseBody): Document {
        return value.use { value ->
            Jsoup.parse(value.byteStream(), "UTF-8", baseUrl)
        }
    }
}