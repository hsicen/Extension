@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.hsicen.library.extensions

import android.content.Context
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.hsicen.library.utils.AppContextHolder

/**
 * 作者：hsicen  3/20/21 3:42 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：资源功能扩展
 */

inline fun Context.dimen(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)
inline fun Fragment.dimen(@DimenRes resId: Int) = context?.dimen(resId) ?: 0
inline fun View.dimen(@DimenRes resId: Int) = context.dimen(resId)

inline fun Context.color(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)
inline fun Fragment.color(@ColorRes resId: Int) = context?.color(resId) ?: 0
inline fun View.color(@ColorRes resId: Int) = context.color(resId)
inline fun color(@ColorRes resId: Int) = AppContextHolder.mContext.color(resId)

inline fun Context.drawableRes(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)
inline fun Fragment.drawableRes(@DrawableRes resId: Int) = context?.drawableRes(resId)
inline fun View.drawableRes(@DrawableRes resId: Int) = context.drawableRes(resId)
inline fun drawableRes(@DrawableRes resId: Int) = AppContextHolder.mContext.drawableRes(resId)

inline fun Context.string(@StringRes resId: Int) = getString(resId)
inline fun Fragment.string(@StringRes resId: Int) = context?.getString(resId) ?: ""
inline fun View.string(@StringRes resId: Int) = context.getString(resId)
inline fun string(@StringRes resId: Int) = AppContextHolder.mContext.getString(resId)

inline fun Context.font(@FontRes resId: Int) = ResourcesCompat.getFont(this, resId)
inline fun font(@FontRes resId: Int) = AppContextHolder.mContext.font(resId)
