package com.gnoemes.shikimori.utils.network;

import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class HtmlResponseBodyConverter implements Converter<ResponseBody, Document> {

    private String baseUrl;

    HtmlResponseBodyConverter(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Document convert(@NonNull ResponseBody value) throws IOException {
        try {
            return Jsoup.parse(value.byteStream(), "UTF-8", baseUrl);
        } finally {
            value.close();
        }
    }
}
