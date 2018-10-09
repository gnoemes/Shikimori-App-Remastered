package com.gnoemes.shikimori.entity.roles.data

import com.google.gson.annotations.SerializedName

data class SeyuRoleResponse(
        @field:SerializedName("characters") val characters: List<CharacterResponse>
)