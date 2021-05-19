package com.hsicen.library.extensions

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.Window

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

fun Window.hideSystemUi() {
    var flags = decorView.systemUiVisibility
    flags = flags or (
            // 无论使用哪种全屏模式，都必须含有下面两个 flag
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
                    // 防止布局随着系统栏的隐藏和显示调整大小
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    // 沉浸模式: 适用于用户将与屏幕进行大量互动的应用,当用户需要调出系统栏时,他们可从隐藏系统栏的任一边滑动
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
            )
    decorView.systemUiVisibility = flags
}


fun Window.fullScreen() {
    var options = decorView.systemUiVisibility
    options = options or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    decorView.systemUiVisibility = options
}

fun View.hideSystemUi() {
    var flags = systemUiVisibility
    flags = flags or (
            // 无论使用哪种全屏模式，都必须含有下面两个 flag
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
                    // 防止布局随着系统栏的隐藏和显示调整大小
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    // 沉浸模式: 适用于用户将与屏幕进行大量互动的应用,当用户需要调出系统栏时,他们可从隐藏系统栏的任一边滑动
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
    systemUiVisibility = flags
}
