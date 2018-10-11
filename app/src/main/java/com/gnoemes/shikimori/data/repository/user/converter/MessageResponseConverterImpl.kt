package com.gnoemes.shikimori.data.repository.user.converter

import com.gnoemes.shikimori.data.repository.common.LinkedContentResponseConverter
import com.gnoemes.shikimori.entity.user.data.MessageResponse
import com.gnoemes.shikimori.entity.user.domain.Message
import javax.inject.Inject

class MessageResponseConverterImpl @Inject constructor(
        private val userConverter: UserBriefResponseConverter,
        private val linkedConverter: LinkedContentResponseConverter
) : MessageResponseConverter {

    override fun apply(t: List<MessageResponse>): List<Message> = t.map { convertResponse(it) }

    private fun convertResponse(it: MessageResponse): Message = Message(
            it.id,
            it.type,
            it.read,
            it.body,
            it.htmlBody,
            it.dateCreated,
            linkedConverter.convertResponse(it.linked),
            userConverter.convertResponse(it.userFrom)!!,
            userConverter.convertResponse(it.userTo)!!
    )
}