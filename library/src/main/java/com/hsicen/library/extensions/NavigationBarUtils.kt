package com.hsicen.library.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.FitWindowsLinearLayout

/**
 * 作者：hsicen  3/20/21 4:04 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：虚拟导航栏工具类
 */
object NavigationBarUtils {

    /*** false 关闭了NavigationBar ,true 开启了*/
    fun navigationBarIsOpen(context: Context): Boolean {
        val rootLinearLayout = findRootLinearLayout(context)
        var navigationBarHeight = 0
        if (rootLinearLayout != null) {
            val layoutParams = rootLinearLayout.layoutParams as MarginLayoutParams
            navigationBarHeight = layoutParams.bottomMargin
        }

        return navigationBarHeight != 0
    }


    /*** 导航栏高度，关闭的时候返回0,开启时返回对应值*/
    fun getNavigationBarHeight(context: Context): Int {
        val rootLinearLayout = findRootLinearLayout(context)
        var navigationBarHeight = 0
        if (rootLinearLayout != null) {
            val layoutParams = rootLinearLayout.layoutParams as MarginLayoutParams
            navigationBarHeight = layoutParams.bottomMargin
        }
        return navigationBarHeight
    }

    /**
     * 从R.id.content从上遍历，拿到 DecorView 下的唯一子布局LinearLayout
     * 获取对应的bottomMargin 即可得到对应导航栏的高度，0为关闭了或没有导航栏
     */
    private fun findRootLinearLayout(context: Context): ViewGroup? {
        var onlyLinearLayout: ViewGroup? = null
        try {
            val window = getWindow(context)
            window?.let {
                val decorView = it.decorView as ViewGroup
                val activity = getActivity(context)
                val contentView = activity?.findViewById<View>(android.R.id.content)
                contentView?.let { cv ->
                    var tempView = cv
                    while (tempView.parent !== decorView) {
                        val parent = tempView.parent as ViewGroup
                        if (parent is LinearLayout) {
                            if (parent is FitWindowsLinearLayout) {
                                tempView = parent
                                continue
                            } else {
                                onlyLinearLayout = parent
                                break
                            }
                        } else {
                            tempView = parent
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return onlyLinearLayout
    }

    private fun getWindow(context: Context): Window? {
        return if (getAppCompActivity(context) != null) {
            getAppCompActivity(context)?.window
        } else {
            scanForActivity(context)?.window
        }
    }

    private fun getActivity(context: Context): Activity? {
        return if (getAppCompActivity(context) != null) {
            getAppCompActivity(context)
        } else {
            scanForActivity(context)
        }
    }

    private fun getAppCompActivity(context: Context?): AppCompatActivity? {
        if (context == null) return null
        if (context is AppCompatActivity) {
            return context
        } else if (context is ContextThemeWrapper) {
            return getAppCompActivity(context.baseContext)
        }
        return null
    }

    private fun scanForActivity(context: Context?): Activity? {
        if (context == null) return null
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return scanForActivity(context.baseContext)
        }
        return null
    }
}