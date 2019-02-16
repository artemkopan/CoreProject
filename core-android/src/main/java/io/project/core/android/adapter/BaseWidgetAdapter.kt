package io.project.core.android.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.project.core.app.lazyNonSafety

abstract class BaseWidgetAdapter<T> : BaseAdapter(), Filterable {

    var onPerformFiltering: ((query: CharSequence?) -> Unit)? = null

    var isFilterEnable = true

    protected var items = emptyList<T>()

    fun set(items: List<T>, notify: Boolean = true) {
        this.items = items
        if (notify) {
            notifyDataSetChanged()
        }
    }

    override fun getItem(position: Int): T = items[position]

    fun getItem(adapterView: AdapterView<out Adapter>): T {
        return getItem(adapterView.selectedItemPosition)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

    abstract override fun getView(position: Int, convertView: View?, parent: ViewGroup): View

    abstract override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View

    open fun convertResultToString(resultValue: Any?): CharSequence {
        return resultValue.toString()
    }

    override fun getFilter(): Filter = defaultFilter

    private val defaultFilter by lazyNonSafety {
        object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                if (isFilterEnable) onPerformFiltering?.invoke(constraint)
                return null
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // no-op
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return this@BaseWidgetAdapter.convertResultToString(resultValue)
            }
        }
    }
}