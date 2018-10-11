package com.gnoemes.shikimori.data.repository.topic.converter

import com.gnoemes.shikimori.entity.forum.data.ForumResponse
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class ForumResponseConverterImpl @Inject constructor() : ForumResponseConverter {

    override fun apply(t: List<ForumResponse>): List<Forum> = t.map { convertResponse(it) }

    override fun convertResponse(it: ForumResponse): Forum = Forum(
            it.id, it.name, it.type, it.url.appendHostIfNeed()
    )
}