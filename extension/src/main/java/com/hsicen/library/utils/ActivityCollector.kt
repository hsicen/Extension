package com.hsicen.library.utils

import android.app.Activity

/**
 * 作者：hsicen  2020/7/5 11:36
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：Activity管理类
 */
object ActivityCollector {

    private val activities = ArrayList<Activity>()

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun finishAll() {
        activities.forEach { act ->
            if (act.isFinishing.not()) {
                act.finish()
            }
        }

        activities.clear()
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}