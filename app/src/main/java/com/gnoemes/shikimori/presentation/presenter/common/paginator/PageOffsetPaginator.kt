package com.gnoemes.shikimori.presentation.presenter.common.paginator

import io.reactivex.Single
import io.reactivex.disposables.Disposable

class PageOffsetPaginator<T>(
        private val requestFactory: (page: Int) -> Single<List<T>>,
        private val viewController: ViewController<T>
) : Paginator {

    private val defaultPage = 1
    private var currentPage: Int = defaultPage

    private var currentState: State<T> = EMPTY()

    private val currentData = mutableListOf<T>()
    private var disposable: Disposable? = null

    override fun restart() = currentState.restart()
    override fun refresh() = currentState.refresh()
    override fun loadNewPage() = currentState.loadNewPage()
    override fun release() = currentState.release()

    private fun loadPage(page: Int) {
        disposable?.dispose()
        disposable = requestFactory.invoke(page)
                .subscribe(
                        { currentState.newData(it) },
                        { currentState.error(it) }
                )
    }

    private fun increasePage() {
        currentPage += 1
    }


    private inner class EMPTY : State<T> {

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyProgress(true)
            loadPage(defaultPage)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class EMPTY_PROGRESS : State<T> {

        override fun restart() {
            loadPage(defaultPage)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentData.clear()
                currentData.addAll(data)
                currentPage = defaultPage
                viewController.showData(true, currentData)
                viewController.showEmptyProgress(false)
            } else {
                currentState = EMPTY_DATA()
                viewController.showEmptyProgress(false)
                viewController.showEmptyView(true)
            }
        }

        override fun error(error: Throwable) {
            currentState = EMPTY_ERROR()
            viewController.showEmptyProgress(false)
            viewController.showEmptyError(true, error)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class EMPTY_ERROR : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyError(false)
            viewController.showEmptyProgress(true)
            loadPage(defaultPage)
        }

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyError(false)
            viewController.showEmptyProgress(true)
            loadPage(defaultPage)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class EMPTY_DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyView(false)
            viewController.showEmptyProgress(true)
            loadPage(defaultPage)
        }

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyView(false)
            viewController.showEmptyProgress(true)
            loadPage(defaultPage)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showEmptyProgress(true)
            loadPage(defaultPage)
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showRefreshProgress(true)
            loadPage(defaultPage)
        }

        override fun loadNewPage() {
            currentState = PAGE_PROGRESS()
            viewController.showPageProgress(true)
            increasePage()
            loadPage(currentPage)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class REFRESH : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showRefreshProgress(false)
            viewController.showEmptyProgress(true)
            loadPage(defaultPage)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentData.clear()
                currentData.addAll(data)
                currentPage = defaultPage
                viewController.showRefreshProgress(false)
                viewController.showData(true, currentData)
            } else {
                currentState = EMPTY_DATA()
                currentData.clear()
                viewController.showData(false)
                viewController.showRefreshProgress(false)
                viewController.showEmptyView(true)
            }
        }

        override fun error(error: Throwable) {
            currentState = DATA()
            viewController.showRefreshProgress(false)
            viewController.showError(error)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class PAGE_PROGRESS : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showPageProgress(false)
            viewController.showEmptyProgress(true)
            loadPage(defaultPage)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentData.addAll(data)
                viewController.showPageProgress(false)
                viewController.showData(true, currentData)
            } else {
                currentState = ALL_DATA()
                viewController.showPageProgress(false)
                viewController.onAllData()
            }
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showPageProgress(false)
            viewController.showRefreshProgress(true)
            loadPage(defaultPage)
        }

        override fun error(error: Throwable) {
            currentState = DATA()
            viewController.showPageProgress(false)
            viewController.showError(error)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class ALL_DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showEmptyProgress(true)
            loadPage(defaultPage)
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showRefreshProgress(true)
            loadPage(defaultPage)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class RELEASED : State<T>
}