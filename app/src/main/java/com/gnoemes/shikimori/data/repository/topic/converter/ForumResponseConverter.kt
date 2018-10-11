package com.gnoemes.shikimori.data.repository.topic.converter

import com.gnoemes.shikimori.entity.forum.data.ForumResponse
import com.gnoemes.shikimori.entity.forum.domain.Forum
import io.reactivex.functions.Function

interface ForumResponseConverter : Function<List<ForumResponse>, List<Forum>> {

    fun convertResponse(it: ForumResponse): Forum
}