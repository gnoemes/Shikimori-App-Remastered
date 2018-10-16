package com.gnoemes.shikimori.utils.network;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import androidx.annotation.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class PlayShikimoriConverterFactory extends Converter.Factory {

    public PlayShikimoriConverterFactory() {
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new HtmlResponseBodyConverter(retrofit.baseUrl().toString());
    }
}
