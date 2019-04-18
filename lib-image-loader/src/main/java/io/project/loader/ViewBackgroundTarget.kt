package io.project.loader

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

open class ViewBackgroundTarget(view: View) : CustomViewTarget<View, Drawable>(view), Transition.ViewAdapter {

    override fun getCurrentDrawable(): Drawable? {
        return view.background
    }

    override fun setDrawable(drawable: Drawable?) {
        view.background = drawable
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        view.background = errorDrawable
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        view.background = placeholder
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        view.background = resource
    }
}