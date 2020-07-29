package com.hsicen.library.extensions

import android.content.Context
import android.content.res.Configuration

/**
 * 作者：hsicen  2020/7/29 9:48
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：主题相关扩展
 */

fun isDarkTheme(ctx: Context): Boolean {
    val flag = ctx.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
    return flag == Configuration.UI_MODE_NIGHT_YES
}
