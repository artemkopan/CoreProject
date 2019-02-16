package io.image.loader

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import io.image.loader.runtime.R

const val NO_OVERRIDE = -1


fun createRequestOptions(
    @Px width: Int = NO_OVERRIDE,
    @Px height: Int = NO_OVERRIDE,
    errorDrawable: GlideHolder = GlideHolder.Res(R.drawable.image_error_default),
    placeholderDrawable: GlideHolder = GlideHolder.Res(R.drawable.image_placeholder_default),
    centerCrop: Boolean = true,
    skipMemoryCache: Boolean = false,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC,
    vararg transformations: Transformation<Bitmap>
): RequestOptions {

    val options = RequestOptions()

    when (errorDrawable) {
        is Drawable -> options.error(errorDrawable)
        is GlideHolder.Res -> options.error(errorDrawable.res)
    }

    when (placeholderDrawable) {
        is Drawable -> options.placeholder(placeholderDrawable)
        is GlideHolder.Res -> options.placeholder(placeholderDrawable.res)
    }

    options.fitCenter()

    if (centerCrop && transformations.isEmpty()) {
        options.centerCrop()
    } else if (centerCrop && transformations.isNotEmpty()) {
        options.transform(MultiTransformation(CenterCrop(), *transformations))
    } else if (transformations.isNotEmpty()) {
        options.transform(MultiTransformation(*transformations))
    }

    if (width != NO_OVERRIDE && height != NO_OVERRIDE) {
        options.override(width, height)
    }

    if (skipMemoryCache) {
        options.skipMemoryCache(true)
    }

    options.diskCacheStrategy(diskCacheStrategy)

    return options
}

fun ImageView.loadCircle(
    model: Any?,
    source: GlideSource = GlideSource.Context(context.applicationContext),
    errorDrawable: GlideHolder = GlideHolder.Res(R.drawable.image_circle_error_default),
    placeholderDrawable: GlideHolder = GlideHolder.Res(R.drawable.image_circle_placeholder_default)
) {
    loadImage(
        model = model,
        source = source,
        errorDrawable = errorDrawable,
        placeholderDrawable = placeholderDrawable,
        diskCacheStrategy = DiskCacheStrategy.ALL,
        transformations = *arrayOf(CircleCrop())
    )
}

@SuppressLint("CheckResult")
fun ImageView.loadImage(
    source: GlideSource = GlideSource.Context(context.applicationContext),
    model: Any?,
    @Px width: Int = NO_OVERRIDE,
    @Px height: Int = NO_OVERRIDE,
    errorDrawable: GlideHolder = GlideHolder.Res(R.drawable.image_error_default),
    placeholderDrawable: GlideHolder = GlideHolder.Res(R.drawable.image_placeholder_default),
    centerCrop: Boolean = true,
    circleCrop: Boolean = false,
    animate: Boolean = false,
    skipMemoryCache: Boolean = false,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC,
    requestListener: RequestListener<Drawable>? = null,
    vararg transformations: Transformation<Bitmap>
) {

    if (model == null) {
        when (errorDrawable) {
            is Drawable -> this.setImageDrawable(errorDrawable)
            is GlideHolder.Res -> this.setImageResource(errorDrawable.res)
        }
        return
    }

    val request = when (source) {
        is GlideSource.Context -> Glide.with(source.context)
        is GlideSource.View -> Glide.with(source.view)
        is GlideSource.Activity -> Glide.with(source.activity)
        is GlideSource.Fragment -> Glide.with(source.fragment)
    }
        .asDrawable()
        .load(model)


    val options = createRequestOptions(
        width, height, errorDrawable, placeholderDrawable, centerCrop,
        skipMemoryCache, diskCacheStrategy, *transformations
    )

    if (animate) {
        request.transition(DrawableTransitionOptions.withCrossFade())
    } else {
        options.dontAnimate()
    }

    if (circleCrop) {
        options.circleCrop()
    }


    if (requestListener != null) {
        request.listener(requestListener)
    }

    request.apply(options)
    request.into(this)
}

fun ImageView.loadClear(source: GlideSource = GlideSource.Context(context.applicationContext)) {
    when (source) {
        is GlideSource.Context -> Glide.with(source.context)
        is GlideSource.View -> Glide.with(source.view)
        is GlideSource.Activity -> Glide.with(source.activity)
        is GlideSource.Fragment -> Glide.with(source.fragment)
    }.clear(this)
}

sealed class GlideSource {
    class Context(val context: android.content.Context) : GlideSource()
    class View(val view: android.view.View) : GlideSource()
    class Activity(val activity: android.app.Activity) : GlideSource()
    class Fragment(val fragment: androidx.fragment.app.Fragment) : GlideSource()
}

sealed class GlideHolder {
    class Drawable(val drawable: android.graphics.drawable.Drawable = ColorDrawable(Color.GRAY)) :
        GlideHolder()

    class Res(@DrawableRes val res: Int) : GlideHolder()

    object Empty : GlideHolder()
}
