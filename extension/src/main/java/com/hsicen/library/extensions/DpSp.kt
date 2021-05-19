package com.hsicen.library.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.hsicen.library.utils.AppContextHolder

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

/***  获取屏幕高度*/
fun screenHeight(): Int = AppContextHolder.mContext.resources.displayMetrics.heightPixels

/*** 获取屏幕宽度*/
fun screenWidth(): Int = AppContextHolder.mContext.resources.displayMetrics.widthPixels

inline fun Context.dp2px(dp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

inline fun Context.dp2px(dp: Int) = dp2px(dp.toFloat()).toInt()

inline fun Context.sp2px(sp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)

inline fun Context.sp2px(sp: Int) = sp2px(sp.toFloat()).toInt()

inline fun Context.px2dp(px: Int) = px / resources.displayMetrics.density
inline fun Context.px2sp(px: Int) = px / resources.displayMetrics.scaledDensity
