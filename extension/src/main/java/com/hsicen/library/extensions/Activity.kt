package com.hsicen.library.extensions

import android.content.Context
import android.content.Intent

/**
 * 作者：hsicen  2020/7/19 18:45
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：Activity扩展
 */

inline fun <reified T> toActivity(context: Context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}
