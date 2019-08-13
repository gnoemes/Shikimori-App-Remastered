package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.common.data.RolesResponse
import com.gnoemes.shikimori.entity.common.domain.Roles
import io.reactivex.functions.Function

interface RolesResponseConverter : Function<List<RolesResponse>, Roles>