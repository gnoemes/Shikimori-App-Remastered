package com.gnoemes.shikimori.entity.app.domain

data class Task(
        val delay: Long = Constants.TASK_LONG_DELAY,
        val action: () -> Unit
)