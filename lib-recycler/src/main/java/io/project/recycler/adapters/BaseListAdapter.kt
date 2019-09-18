package io.project.recycler.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.project.recycler.holders.BaseHolder
import java.util.Collections

@Suppress("unused", "MemberVisibilityCanBePrivate", "LeakingThis", "RedundantOverride")
abstract class BaseListAdapter<T, VH : BaseHolder<T>> : RecyclerView.Adapter<VH>(), BaseAdapter<T, VH> {

    protected var list: List<T> = emptyList()
    private val helper = BaseAdapterHelper(this)

    override fun setClickEvent(func: (containerView: View, viewId: Int, pos: Int, item: T?) -> Unit) {
        helper.clickEvent = func
    }

    override fun showFooter(isShow: Boolean) {
        helper.showFooter = isShow
    }

    //region Create and Bind methods

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return helper.onCreateViewHolder(parent, viewType)
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup, viewType: Int): VH {
        throw NotImplementedError("method in not implemented")
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        helper.onBindViewHolder(holder, position)
    }

    final override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any>) {
        helper.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindItemViewHolder(holder: VH, position: Int, payloads: List<Any>) {
        holder.bind(getItem(position)!!, payloads)
    }

    override fun onBindFooterViewHolder(holder: VH, position: Int) {
        // override method for implementation
    }

    //endregion

    //region Holder LifeCycle methods

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        helper.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        super.onViewDetachedFromWindow(holder)
        helper.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: VH) {
        helper.onViewRecycled(holder)
    }

    //endregion

    open fun setList(list: List<T>, notifyChanged: Boolean) {
        this.list = Collections.unmodifiableList(list)
        if (notifyChanged) notifyDataSetChanged()
    }

    override fun getItem(position: Int): T? {
        val itemPosition = helper.getItemPosition(position)
        return if (itemPosition >= list.size || list.isEmpty()) null else list[itemPosition]
    }

    override fun getItemCount(): Int {
        return helper.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return helper.getItemViewType(position)
    }

    override fun getRealSize(): Int = list.size

    override fun isEmpty() = itemCount == 0
}