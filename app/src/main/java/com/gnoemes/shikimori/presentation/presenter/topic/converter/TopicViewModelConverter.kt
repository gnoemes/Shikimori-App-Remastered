package com.gnoemes.shikimori.presentation.presenter.topic.converter

import com.gnoemes.shikimori.entity.topic.domain.Topic
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import io.reactivex.functions.Function

interface TopicViewModelConverter : Function<List<Topic>, List<TopicViewModel>>