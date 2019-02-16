package io.project.recycler.adapters

import android.view.View
import android.view.ViewGroup
import io.project.recycler.holders.BaseHolder
import kotlin.properties.Delegates

const val FOOTER = Int.MIN_VALUE + 1

interface BaseAdapter<T, VH : BaseHolder<T>> {

    fun setClickEvent(func: (containerView: View, viewId: Int, pos: Int, item: T?) -> Unit)

    fun showFooter(isShow: Boolean)

    fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): VH

    fun onCreateFooterViewHolder(parent: ViewGroup, viewType: Int): VH

    fun onBindItemViewHolder(holder: VH, position: Int, payloads: List<Any>)

    fun onBindFooterViewHolder(holder: VH, position: Int)

    fun getRealSize(): Int

    fun getItem(position: Int): T?

    fun isEmpty(): Boolean

    fun notifyItemInserted(position: Int)

    fun notifyItemRemoved(position: Int)

}

class BaseAdapterHelper<T, VH : BaseHolder<T>>(private val adapter: BaseAdapter<T, VH>) {

    var clickEvent: ((containerView: View, viewId: Int, pos: Int, item: T?) -> Unit)? = null

    var showFooter: Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        if (oldValue == newValue) {
            return@observable
        }
        if (newValue) {
            adapter.notifyItemInserted(getItemCount())
        } else {
            adapter.notifyItemRemoved(getItemCount())
        }
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return when (viewType) {
            FOOTER -> adapter.onCreateFooterViewHolder(parent, viewType)
            else -> adapter.onCreateItemViewHolder(parent, viewType)
        }
    }

    fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any> = emptyList()) {
        holder.bindClickListener(View.OnClickListener { view ->
            val pos = holder.adapterPosition
            clickEvent?.invoke(holder.itemView, view.id, pos, adapter.getItem(pos))
        })
        return when {
            position >= adapter.getRealSize() -> adapter.onBindFooterViewHolder(holder, position)
            else -> adapter.onBindItemViewHolder(holder, position, payloads)
        }
    }

    fun getItemCount(): Int {
        var additionalSize = 0
        if (showFooter) {
            additionalSize += 1
        }
        return additionalSize + adapter.getRealSize()
    }

    fun getItemViewType(position: Int): Int {
        return when {
            position >= adapter.getRealSize() -> FOOTER  //todo check this
            else -> 0
        }
    }

    fun onViewAttachedToWindow(holder: VH) {
        holder.onViewAttachedToWindow()
    }

    fun onViewDetachedFromWindow(holder: VH) {
        holder.onViewDetachedFromWindow()
    }

    fun onViewRecycled(holder: VH) {
        holder.recycled()
        holder.clearViewCache()
    }

    fun getItemPosition(position: Int): Int {
        return position
    }
}