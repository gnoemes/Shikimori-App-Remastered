package com.gnoemes.shikimori.data.repository.app

import com.gnoemes.shikimori.entity.app.domain.Task

interface TaskRepository {
    fun newTask(task: Task): Int
    fun executeTask(id : Int)
    fun deleteTask(id : Int)
}