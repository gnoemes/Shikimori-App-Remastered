package com.gnoemes.shikimori.data.repository.user.converter

import com.gnoemes.shikimori.entity.user.data.MessageResponse
import com.gnoemes.shikimori.entity.user.domain.Message
import io.reactivex.functions.Function

interface MessageResponseConverter : Function<List<MessageResponse>, List<Message>>