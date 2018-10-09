package ru.arkell.avarkom.extensions

import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxHelper {
  fun <T> applyObservableScheduler(): ObservableTransformer<T, T> {
    return ObservableTransformer<T, T> { upstream ->
      upstream.subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
    }
  }

  fun <T> applySingleScheduler(): SingleTransformer<T, T> {
    return SingleTransformer<T, T> { upstream ->
      upstream.subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
    }
  }
}