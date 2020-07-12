package com.hsicen.library.extensions

import android.content.res.Resources
import android.util.TypedValue

/**
 * <p>作者：Hsicen  2019/7/23 15:25
 * <p>邮箱：codinghuang@163.com
 * <p>作用：
 * <p>描述：dp and sp extension
 */

val Float.sp2px: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )

val Float.dp2px: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

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

val Int.dp2px: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this * 1.0f, Resources.getSystem().displayMetrics
    ).toInt()
