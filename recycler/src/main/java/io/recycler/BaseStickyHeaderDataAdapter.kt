package com.artemkopan.presentation.base.recycler

import android.view.ViewGroup
import io.recycler.adapters.BaseDataAdapter
import io.recycler.holders.BaseHolder
import java.util.*

abstract class BaseStickyHeaderDataAdapter<T, VH : BaseHolder<AdapterEntity<T>>>(diffCallback: DiffUtil.ItemCallback<AdapterEntity<T>>) : BaseDataAdapter<AdapterEntity<T>, VH>(diffCallback), StickyHeaderHandler where T : DiffEntity<T> {

    private var items = emptyList<AdapterEntity<T>>()

    override fun submitList(list: List<AdapterEntity<T>>?) {
        super.submitList(list)
        items = Collections.unmodifiableList(list)
    }

    override fun getAdapterData(): List<*> = items

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)?.isHeader == true) {
            HEADER_TYPE
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolderProcess(parent: ViewGroup, viewType: Int): VH {
        return if (viewType == HEADER_TYPE) {
            onStickyHeaderCreateViewHolder(parent, viewType)
        } else {
            super.onCreateViewHolderProcess(parent, viewType)
        }
    }

    override fun onBindViewHolderProcess(holder: VH, position: Int, payloads: List<Any>) {
        if (getItemViewType(position) == HEADER_TYPE) {
            onBindStickyHeaderViewHolder(holder, position, getItem(position)?.model, payloads)
        } else {
            super.onBindViewHolderProcess(holder, position, payloads)
        }
    }

    abstract fun onStickyHeaderCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindStickyHeaderViewHolder(holder: VH, position: Int, model: T?, payloads: List<Any>)
}