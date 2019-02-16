package io.project.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import io.project.core.app.DiffEntity

open class SimpleDiffUtil<T : DiffEntity<T>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(p0: T, p1: T): Boolean {
        return p0.areItemsTheSame(p1)
    }

    override fun areContentsTheSame(p0: T, p1: T): Boolean {
        return p0.areContentsTheSame(p1)
    }
}