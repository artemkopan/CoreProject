package io.project.recycler.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

// Do not use CACHE in base classes because it creates unused HashMap.
// An each inherit holder creates new cache map
@ContainerOptions(CacheImplementation.NO_CACHE)
abstract class BaseHolder<T>(val containerView: View) : RecyclerView.ViewHolder(containerView){
//        , LayoutContainer {

    open fun onViewAttachedToWindow() {}

    abstract fun bind(item: T, payloads: List<Any>)

    open fun onViewDetachedFromWindow() {}

    abstract fun bindClickListener(listener: View.OnClickListener)

    open fun recycled() {}

    fun clearViewCache() {
//        clearFindViewByIdCache()
    }

}
