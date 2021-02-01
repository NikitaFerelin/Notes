package com.ferelin.notes.utilits

import android.widget.Filter
import java.util.*

abstract class AppFilter<T> : Filter() {

    abstract val items: MutableList<T>
    abstract fun filterStrategy(item: T, text: String): Boolean

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val filterList = mutableListOf<T>()
        val currentSearchText = constraint.toString().toLowerCase(Locale.ROOT)

        if (currentSearchText.isNotEmpty()) {
            this.filter(currentSearchText, filterList)
        } else filterList.addAll(items)

        return FilterResults().apply { values = filterList }
    }

    private fun filter(searchText: String, filterList: MutableList<T>) {
        for (item in items) {
            if (filterStrategy(item, searchText)) {
                filterList.add(item)
            }
        }
    }
}