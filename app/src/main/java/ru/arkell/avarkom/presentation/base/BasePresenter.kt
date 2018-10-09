package ru.arkell.avarkom.presentation.base

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<View> {

  protected var view: View? = null
  protected var subscriptions: CompositeDisposable? = null

  open fun onStart() {
  }

  open fun onStop() {
  }

  open fun onAttach(view: View) {
    subscriptions = CompositeDisposable()
    this.view = view
  }

  open fun onDetach() {
    this.view = null
    this.subscriptions?.dispose()
    this.subscriptions?.clear()
  }
}
