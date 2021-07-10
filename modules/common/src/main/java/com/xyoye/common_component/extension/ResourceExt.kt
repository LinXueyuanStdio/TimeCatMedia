package com.xyoye.common_component.extension

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.timecat.extend.arms.BaseApplication
import com.xyoye.common_component.R

/**
 * Created by xyoye on 2021/3/20.
 */

fun Int.toResColor(): Int{
    return ContextCompat.getColor(BaseApplication.getContext(), this)
}

fun Int.toResDrawable(): Drawable?{
    return ContextCompat.getDrawable(BaseApplication.getContext(), this)
}

fun Int.toResString(): String{
    return BaseApplication.getContext().resources.getString(this)
}

fun rippleDrawable(@ColorRes rippleColorId: Int = R.color.gray_40): Drawable{
    return RippleDrawable(ColorStateList.valueOf(rippleColorId.toResColor()), null, null)
}