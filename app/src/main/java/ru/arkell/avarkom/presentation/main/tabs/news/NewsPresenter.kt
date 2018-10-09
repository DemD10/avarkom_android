package ru.arkell.avarkom.presentation.main.tabs.news

import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.extensions.RxHelper
import ru.arkell.avarkom.presentation.base.BasePresenter
import javax.inject.Inject

class NewsPresenter @Inject constructor(
    var networkService: AvarkomService) : BasePresenter<NewsView>() {
  fun getNews() {
    view?.showLoading()
    subscriptions?.add(networkService.getNews("", 30, 0)
        .compose(RxHelper.applySingleScheduler())
        .subscribe({
          view?.hideLoading()
          view?.showNews(it)
        }, {
          view?.hideLoading()
          view?.showError()
        }))

  }
}