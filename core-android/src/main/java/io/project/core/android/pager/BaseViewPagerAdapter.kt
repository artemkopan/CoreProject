package io.project.core.android.pager

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

abstract class BaseViewPagerAdapter<T>(items: List<T> = emptyList()) : PagerAdapter() {

    private val items: MutableList<T> = mutableListOf()

    init {
        this.items.addAll(items)
    }

    fun addItem(item: T) = items.add(item)
    fun addItems(items: Iterable<T>) = this.items.addAll(items)
    fun getItem(position: Int) = items[position]
    fun clearItems() = items.clear()

    abstract fun onCreateView(container: ViewGroup, position: Int, item: T): View

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return onCreateView(container, position, items[position]).also { container.addView(it) }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = items.size


}