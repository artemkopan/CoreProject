package io.recycler.holders

import android.view.View

open class SimpleHolder<T>(containerView: View) : BaseHolder<T>(containerView) {

    override fun bind(item: T, payloads: List<Any>) {}

    override fun bindClickListener(listener: View.OnClickListener) {}

}