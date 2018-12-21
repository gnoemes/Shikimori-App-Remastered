package com.gnoemes.shikimori.entity.common.presentation.shikimori

import com.google.gson.annotations.SerializedName

abstract class Content(@SerializedName("contentType") val contentType: ContentType)