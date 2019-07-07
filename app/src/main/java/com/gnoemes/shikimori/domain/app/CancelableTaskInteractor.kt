package com.gnoemes.shikimori.domain.app

import com.gnoemes.shikimori.entity.app.domain.Task
import io.reactivex.Completable
import io.reactivex.Observable

interface CancelableTaskInteractor {
    fun newTask(task: Task) : Observable<Int>
    fun cancelTask(id : Int) : Completable
}