package com.hsicen.library.extensions

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * 作者：hsicen  4/5/21 11:35 AM
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：SnackBar扩展
 */

inline fun View.snackbar(msg: Int, @StringRes actionText: Int, noinline action: (View) -> Unit) =
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT)
        .setAction(actionText, action)
        .apply { show() }

