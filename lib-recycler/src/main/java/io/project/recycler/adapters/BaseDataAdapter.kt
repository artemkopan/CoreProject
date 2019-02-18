package io.project.recycler.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.project.recycler.holders.BaseHolder

@Suppress("unused", "MemberVisibilityCanBePrivate", "LeakingThis", "RedundantOverride")
abstract class BaseDataAdapter<T, VH : BaseHolder<T>> : ListAdapter<T, VH>, BaseAdapter<T, VH> {

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : super(diffCallback)
    constructor(config: AsyncDifferConfig<T>) : super(config)

    private val helper = BaseAdapterHelper(this)

    override fun setClickEvent(func: (containerView: View, viewId: Int, pos: Int, item: T?) -> Unit) {
        helper.clickEvent = func
    }

    override fun showFooter(isShow: Boolean) {
        helper.showFooter = isShow
    }

    //region Create and Bind methods

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolderProcess(parent, viewType)
    }

    protected open fun onCreateViewHolderProcess(parent: ViewGroup, viewType: Int): VH {
        return helper.onCreateViewHolder(parent, viewType)
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup, viewType: Int): VH {
        throw NotImplementedError("method in not implemented")
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolderProcess(holder, position, emptyList())
    }

    final override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any>) {
        onBindViewHolderProcess(holder, position, payloads)
    }

    protected open fun onBindViewHolderProcess(holder: VH, position: Int, payloads: List<Any>) {
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

    override fun getItem(position: Int): T? {
        return super.getItem(helper.getItemPosition(position))
    }

    override fun getItemCount(): Int {
        return helper.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return helper.getItemViewType(position)
    }

    override fun getRealSize(): Int = super.getItemCount()

    override fun isEmpty() = itemCount == 0
}