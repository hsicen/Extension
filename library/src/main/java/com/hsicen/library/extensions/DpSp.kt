package com.hsicen.library.extensions

import android.content.res.Resources
import android.util.TypedValue

/**
 * 作者：hsicen  3/20/21 2:28 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：dp sp px
 */

val Float.px2sp: Float
    get() {
        val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
        return (this / scaledDensity + 0.5f)
    }

val Float.px2dp: Float
    get() {
        val scaledDensity = Resources.getSystem().displayMetrics.density
        return (this / scaledDensity + 0.5f)
    }

val Int.sp2px: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this * 1.0f, Resources.getSystem().displayMetrics
    ).toInt()


val Float.sp2px: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )

val Int.dp2px: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this * 1.0f, Resources.getSystem().displayMetrics
    ).toInt()

val Float.dp2px: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )
