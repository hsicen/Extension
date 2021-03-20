package com.hsicen.library.utils

import android.app.Application

/**
 * 作者：hsicen  3/20/21 3:43 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：Context提供
 */
object AppContextHolder {

    @Volatile
    private var inject = false

    lateinit var mContext: Application
        private set

    fun inject(context: Application) {
        if (inject) {
            throw IllegalStateException("inject can only call once!")
        } else {
            mContext = context
            inject = true
        }
    }
}