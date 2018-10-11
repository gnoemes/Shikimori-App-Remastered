package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.GenreResponseConverter
import com.gnoemes.shikimori.entity.common.data.GenreResponse
import com.gnoemes.shikimori.entity.common.domain.Genre
import javax.inject.Inject

class GenreResponseConverterImpl @Inject constructor() : GenreResponseConverter {

    override fun apply(t: List<GenreResponse>): List<Genre> {
        val genres = mutableListOf<Genre>()

        t.forEach { response ->
            val genre = Genre.values().firstOrNull { it.equalsName(convertGenreName(response.name)) }
            if (genre != null) genres.add(genre)
        }

        return genres
    }

    private fun convertGenreName(name: String): String {
        val builder = StringBuilder()
        name.toCharArray().forEach {
            if (Character.isWhitespace(it) || it == '-') builder.append('_') else builder.append(it)
        }
        return builder.toString().toLowerCase()
    }
}