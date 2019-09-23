package com.gnoemes.shikimori.utils.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.internal.LinkedTreeMap
import java.lang.reflect.Type
import java.util.*
import kotlin.math.ceil

class MapDeserializerDoubleAsIntFix : JsonDeserializer<Map<String, Any?>> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Map<String, Any?> {
        return read(json) as Map<String, Any?>
    }

    private fun read(`in`: JsonElement?): Any? {

        if (`in`?.isJsonArray == true) {
            val list = ArrayList<Any?>()
            val arr = `in`.asJsonArray
            for (anArr in arr) {
                list.add(read(anArr))
            }
            return list
        } else if (`in`?.isJsonObject == true) {
            val map = LinkedTreeMap<String, Any?>()
            val obj = `in`.asJsonObject
            val entitySet = obj.entrySet()
            for ((key, value) in entitySet) {
                map[key] = read(value)!!
            }
            return map
        } else if (`in`?.isJsonPrimitive == true) {
            val prim = `in`.asJsonPrimitive
            if (prim.isBoolean) {
                return prim.asBoolean
            } else if (prim.isString) {
                return prim.asString
            } else if (prim.isNumber) {

                val num = prim.asNumber
                // here you can handle double int/long values
                // and return any type you want
                // this solution will transform 3.0 float to long values
                return if (ceil(num.toDouble()) == num.toLong().toDouble())
                    num.toLong()
                else {
                    num.toDouble()
                }
            }
        }
        return null
    }
}