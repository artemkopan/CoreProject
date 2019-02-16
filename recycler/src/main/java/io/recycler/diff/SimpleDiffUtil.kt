package io.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import io.core.common.DiffEntity

open class SimpleDiffUtil<T : DiffEntity<T>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(p0: T, p1: T): Boolean {
        return p0.areItemsTheSame(p1)
    }

    override fun areContentsTheSame(p0: T, p1: T): Boolean {
        return p0.areContentsTheSame(p1)
    }
}