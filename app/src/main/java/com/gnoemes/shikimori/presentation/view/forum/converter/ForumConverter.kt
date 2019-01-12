package com.gnoemes.shikimori.presentation.view.forum.converter

import com.gnoemes.shikimori.entity.forum.domain.Forum
import io.reactivex.functions.Function

interface ForumConverter : Function<List<Forum>, List<Forum>>