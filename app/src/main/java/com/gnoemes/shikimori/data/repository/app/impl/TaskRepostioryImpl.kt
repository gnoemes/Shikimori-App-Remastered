package com.gnoemes.shikimori.data.repository.app.impl

import com.gnoemes.shikimori.data.repository.app.TaskRepository
import com.gnoemes.shikimori.entity.app.domain.Task
import javax.inject.Inject

class TaskRepostioryImpl @Inject constructor() : TaskRepository {
    private val tasks = mutableListOf<Task>()

    override fun newTask(task: Task): Int {
        tasks.add(task)
        return tasks.indexOf(task)
    }

    override fun executeTask(id: Int) {
        if (tasks.lastIndex >= id) {
            tasks[id].action.invoke()
            deleteTask(id)
        }
    }

    override fun deleteTask(id: Int) {
        if (tasks.lastIndex >= id) {
            tasks.removeAt(id)
        }
    }
}