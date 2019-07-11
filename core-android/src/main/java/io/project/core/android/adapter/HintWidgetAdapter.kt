package io.project.core.android.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView


abstract class HintWidgetAdapter<T>(private val baseAdapter: BaseWidgetAdapter<T>) : BaseWidgetAdapter<T>() {


    abstract fun getHintView(convertView: View?, parent: ViewGroup): View
    abstract fun getDropDownHintView(convertView: View?, parent: ViewGroup): View

    fun set(items: List<T>) {
        set(items, true)
    }

    override fun set(items: List<T>, notify: Boolean) {
        baseAdapter.set(items, notify)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == 0) {
            getHintView(convertView, parent)
        } else {
            baseAdapter.getView(position - 1, convertView, parent)
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == 0) {
            getDropDownHintView(convertView, parent)
        } else {
            baseAdapter.getDropDownView(position - 1, convertView, parent)
        }
    }

    override fun getItem(position: Int): T? = if (position == 0) null else baseAdapter.getItem(position - 1)

    override fun getItemId(position: Int): Long = if (position == 0) 0L else baseAdapter.getItemId(position - 1)

    override fun getCount(): Int = baseAdapter.count + 1

    fun setSelection(view: AdapterView<*>, position: Int) {
        view.setSelection(position + 1)
    }

    fun getRealPosition(position: Int) = position - 1
}

