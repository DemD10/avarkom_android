package ru.arkell.avarkom.presentation.base

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.View
import java.util.ArrayList

/**
 * Base abstract recycler view adapter for lists
 */
abstract class BaseRecyclerAdapter<T> : Adapter<BaseRecyclerAdapter<T>.RecyclerViewHolder<T>>() {
  val dataItems = ArrayList<T>()

  fun setData(items: List<T>) {
    this.dataItems.clear()
    this.dataItems.addAll(items)
    notifyDataSetChanged()
  }

  fun addData(items: List<T>) {
    this.dataItems.addAll(items)
    notifyDataSetChanged()
  }

  fun clearData(){
    this.dataItems.clear()
    notifyDataSetChanged()
  }

  fun getItemAt(position: Int): T {
    return dataItems[position]
  }

  override fun getItemCount(): Int {
    return dataItems.size
  }

  fun addItem(item: T) {
    dataItems.add(item)
    notifyItemInserted(itemCount - 1)
  }

  fun getItems(): List<T> {
    return ArrayList(dataItems)
  }

  override fun onBindViewHolder(holder: RecyclerViewHolder<T>?, position: Int) {
    holder?.setItem(getItemAt(position), position)
  }

  abstract inner class RecyclerViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun setItem(t: T, position: Int)

    protected val getItem: T
      get() = getItemAt(adapterPosition) as T
  }
}
