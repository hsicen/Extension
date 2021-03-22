package com.hsicen.library.toast

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.hsicen.library.utils.AppContextHolder

/**
 * 作者：hsicen  3/22/21 5:23 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：Toast tools
 */
object KToast {

    fun init() {

    }
}


inline fun info(str: String, context: Context = AppContextHolder.mContext) {
    Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
}

inline fun info(@StringRes strId: Int, context: Context = AppContextHolder.mContext) {
    Toast.makeText(context, strId, Toast.LENGTH_SHORT).show()
}
