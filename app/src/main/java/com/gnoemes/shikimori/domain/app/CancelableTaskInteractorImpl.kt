package com.gnoemes.shikimori.domain.app

import com.gnoemes.shikimori.data.repository.app.TaskRepository
import com.gnoemes.shikimori.entity.app.domain.Task
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CancelableTaskInteractorImpl @Inject constructor(
        private val repository: TaskRepository
) : CancelableTaskInteractor {

    override fun newTask(task: Task): Observable<Int> {
        val id = repository.newTask(task)
        return Observable.merge(
                Observable.just(id),
                delayTask(id, task.delay)
        )
    }

    private fun delayTask(id: Int, mills: Long) = Observable.just(id)
            .delay(mills, TimeUnit.MILLISECONDS, Schedulers.single())
            .flatMapCompletable { Completable.fromAction { repository.executeTask(id) } }
            .andThen(Observable.empty<Int>())

    override fun cancelTask(id: Int): Completable = Completable.fromAction { repository.deleteTask(id) }
}