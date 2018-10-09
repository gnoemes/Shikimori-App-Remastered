package com.gnoemes.shikimori.entity.common.data

import com.gnoemes.shikimori.entity.roles.data.CharacterResponse
import com.gnoemes.shikimori.entity.roles.data.PersonResponse
import com.google.gson.annotations.SerializedName

data class RolesResponse(
        @field:SerializedName("roles") val roles: List<String>,
        @field:SerializedName("roles_russian") val rolesRu: List<String>,
        @field:SerializedName("character") val character: CharacterResponse?,
        @field:SerializedName("person") val person: PersonResponse?
)