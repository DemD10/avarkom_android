package ru.arkell.avarkom.presentation.base

import android.support.v7.widget.RecyclerView
import android.widget.Filter
import android.widget.Filterable
import io.reactivex.subjects.PublishSubject

abstract class GenericRecycleAdapter<E : ViewModel>()
  : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
  var onItemSelectAction: PublishSubject<Int> = PublishSubject.create()
  var list: List<E>
  var originalList: List<E>

  init {
    this.originalList = ArrayList<E>()
    this.list = ArrayList<E>()
  }

  override fun getFilter(): Filter {
    return object : Filter() {

      override fun publishResults(constraint: CharSequence, results: FilterResults) {
        list = results.values as List<E>
        this@GenericRecycleAdapter.notifyDataSetChanged()
      }

      override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
        val filteredResults: List<E>?
        if (constraint.isEmpty()) {
          filteredResults = originalList
        } else {
          filteredResults = getFilteredResults(constraint.toString().toLowerCase())
        }

        val results = Filter.FilterResults()
        results.values = filteredResults

        return results
      }
    }
  }

  abstract fun setData(collection: List<E>)

  override fun getItemCount(): Int {
    return list.size
  }

  protected fun getFilteredResults(text: String): List<E> {
    val results = list.filter { it.getName().toLowerCase().contains(text) }

    return results
  }
}

abstract class ViewModel {
  abstract fun getName(): String
  abstract fun getId(): Int
}
