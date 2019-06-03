@file:Suppress("unused")

package io.project.loader

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
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

const val NO_OVERRIDE = -1

private typealias AndroidDrawable = android.graphics.drawable.Drawable

@SuppressLint("CheckResult")
fun createRequestOptions(
    @Px width: Int = NO_OVERRIDE,
    @Px height: Int = NO_OVERRIDE,
    errorDrawable: ImageHolder = ImageHolder.Res(R.drawable.ph_error),
    placeholderDrawable: ImageHolder = ImageHolder.Res(R.drawable.ph_default),
    centerCrop: Boolean = true,
    skipMemoryCache: Boolean = false,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC,
    vararg transformations: Transformation<Bitmap>
): RequestOptions {

    val options = RequestOptions()

    when (errorDrawable) {
        is ImageHolder.Drawable -> options.error(errorDrawable.drawable)
        is ImageHolder.Res -> options.error(errorDrawable.res)
    }

    when (placeholderDrawable) {
        is ImageHolder.Drawable -> options.placeholder(placeholderDrawable.drawable)
        is ImageHolder.Res -> options.placeholder(placeholderDrawable.res)
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
    source: ImageSource = ImageSource.Context(context.applicationContext),
    errorDrawable: ImageHolder = ImageHolder.Res(R.drawable.ph_circle_error),
    placeholderDrawable: ImageHolder = ImageHolder.Res(R.drawable.ph_circle_default)
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
    source: ImageSource = ImageSource.Context(context.applicationContext),
    model: Any?,
    @Px width: Int = NO_OVERRIDE,
    @Px height: Int = NO_OVERRIDE,
    errorDrawable: ImageHolder = ImageHolder.Res(R.drawable.ph_error),
    placeholderDrawable: ImageHolder = ImageHolder.Res(R.drawable.ph_default),
    centerCrop: Boolean = true,
    circleCrop: Boolean = false,
    animate: Boolean = false,
    skipMemoryCache: Boolean = false,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC,
    requestListener: RequestListener<AndroidDrawable>? = null,
    vararg transformations: Transformation<Bitmap>
) {

    if (model == null) {
        when (errorDrawable) {
            is ImageHolder.Drawable -> this.setImageDrawable(errorDrawable.drawable)
            is ImageHolder.Res -> this.setImageResource(errorDrawable.res)
        }
        return
    }

    val request = when (source) {
        is ImageSource.Context -> Glide.with(source.context)
        is ImageSource.View -> Glide.with(source.view)
        is ImageSource.Activity -> Glide.with(source.activity)
        is ImageSource.Fragment -> Glide.with(source.fragment)
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


@SuppressLint("CheckResult")
fun View.loadBackground(
    model: Any?,
    source: ImageSource = ImageSource.Context(context.applicationContext),
    errorDrawable: ImageHolder = ImageHolder.Empty,
    placeholderDrawable: ImageHolder = ImageHolder.Empty,
    animate: Boolean = true,
    @Px width: Int = NO_OVERRIDE,
    @Px height: Int = NO_OVERRIDE,
    centerCrop: Boolean = true,
    skipMemoryCache: Boolean = false,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC,
    vararg transformations: Transformation<Bitmap>
) {

    val request = when (source) {
        is ImageSource.Context -> Glide.with(source.context)
        is ImageSource.View -> Glide.with(source.view)
        is ImageSource.Activity -> Glide.with(source.activity)
        is ImageSource.Fragment -> Glide.with(source.fragment)
    }
        .asDrawable()
        .load(model)

    if (animate) {
        request.transition(DrawableTransitionOptions.withCrossFade())
    }


    val options = createRequestOptions(
        width,
        height,
        errorDrawable,
        placeholderDrawable,
        centerCrop,
        skipMemoryCache,
        diskCacheStrategy,
        *transformations
    )

    request.apply(options)
    request.into(ViewBackgroundTarget(this))
}


fun ImageView.loadClear(source: ImageSource = ImageSource.Context(context.applicationContext)) {
    when (source) {
        is ImageSource.Context -> Glide.with(source.context)
        is ImageSource.View -> Glide.with(source.view)
        is ImageSource.Activity -> Glide.with(source.activity)
        is ImageSource.Fragment -> Glide.with(source.fragment)
    }.clear(this)
}

sealed class ImageSource {
    class Context(val context: android.content.Context) : ImageSource()
    class View(val view: android.view.View) : ImageSource()
    class Activity(val activity: android.app.Activity) : ImageSource()
    class Fragment(val fragment: androidx.fragment.app.Fragment) : ImageSource()
}

sealed class ImageHolder {
    class Drawable(val drawable: AndroidDrawable = ColorDrawable(Color.GRAY)) : ImageHolder()
    class Res(@DrawableRes val res: Int) : ImageHolder()
    object Empty : ImageHolder()
}
