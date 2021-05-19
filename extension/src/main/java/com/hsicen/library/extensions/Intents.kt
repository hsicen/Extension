@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.hsicen.library.extensions

import android.content.Intent

/**
 * 作者：hsicen  3/20/21 3:40 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：Intent功能扩展
 */

inline fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }
inline fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
inline fun Intent.clearWhenTaskReset(): Intent =
    apply { addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT) }

inline fun Intent.excludeFromRecent(): Intent =
    apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

inline fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }
inline fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
inline fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }
inline fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }
inline fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }