package io.project.loader

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.Nullable
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

open class ViewBackgroundTarget(view: View) : CustomViewTarget<View, Drawable>(view), Transition.ViewAdapter {

    private var animatable: Animatable? = null

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
        if (transition == null || !transition.transition(resource, this)) {
            view.background = resource
        } else {
            maybeUpdateAnimatable(resource)
        }
    }

    override fun onStart() {
        animatable?.start()
    }

    override fun onStop() {
        animatable?.stop()
    }

    private fun maybeUpdateAnimatable(@Nullable resource: Drawable) {
        if (resource is Animatable) {
            animatable = resource
            animatable!!.start()
        } else {
            animatable = null
        }
    }
}