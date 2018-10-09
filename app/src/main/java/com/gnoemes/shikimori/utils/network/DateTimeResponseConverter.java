package com.gnoemes.shikimori.utils.network;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;

public interface DateTimeResponseConverter extends JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
}
