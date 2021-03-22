package com.hsicen.extension

import android.app.Application
import com.hsicen.library.utils.AppContextHolder

/**
 * 作者：hsicen  3/22/21 5:37 PM
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：Extension
 */
class KApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppContextHolder.inject(this)
    }

}