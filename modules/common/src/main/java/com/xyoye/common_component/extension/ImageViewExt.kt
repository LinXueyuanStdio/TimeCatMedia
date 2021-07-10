package com.xyoye.common_component.extension

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.xyoye.common_component.utils.dp2px

/**
 * Created by xyoye on 2020/7/31.
 */

fun ImageView.setGlideImage(source: String?, dpRadius: Int = 0, @DrawableRes errorRes: Int = 0) {
    if (dpRadius > 0) {
        Glide.with(this)
            .asBitmap()
            .transition((BitmapTransitionOptions.withCrossFade()))
            .apply(RequestOptions.centerCropTransform().transform(RoundedCorners(dp2px(dpRadius))).error(errorRes))
            .load(source)
            .into(this)
    } else {
        Glide.with(this)
            .load(source)
            .apply(RequestOptions.centerCropTransform().error(errorRes))
            .transition((DrawableTransitionOptions.withCrossFade()))
            .into(this)
    }
}

fun ImageView.setGlideImage(source: Uri?, dpRadius: Int = 0, @DrawableRes errorRes: Int = 0) {
    if (dpRadius > 0) {
        Glide.with(this)
            .asBitmap()
            .load(source)
            .apply(RequestOptions.centerCropTransform().transform(RoundedCorners(dp2px(dpRadius))).error(errorRes))
            .transition((BitmapTransitionOptions.withCrossFade()))
            .into(this)
    } else {
        Glide.with(this)
            .load(source)
            .apply(RequestOptions.centerCropTransform().error(errorRes))
            .transition((DrawableTransitionOptions.withCrossFade()))
            .into(this)
    }
}