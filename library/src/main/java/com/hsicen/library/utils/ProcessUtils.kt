package com.hsicen.library.utils

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.text.TextUtils
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * 作者：hsicen  3/20/21 4:00 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：进程工具类
 */
object ProcessUtils {

    fun isMainProcess(context: Context): Boolean {
        return context.packageName == getCurrentProcessName(context)
    }

    /**
     * Return the name of current process.
     */
    fun getCurrentProcessName(context: Context): String {
        val appContext = context.applicationContext
        var name = currentProcessNameByFile
        if (!TextUtils.isEmpty(name)) return name
        name = getCurrentProcessNameByAms(appContext)
        if (!TextUtils.isEmpty(name)) return name
        name = getCurrentProcessNameByReflect(context)
        return name
    }

    private val currentProcessNameByFile: String
        get() = try {
            val file = File("/proc/" + Process.myPid() + "/" + "cmdline")
            val bufferedReader = BufferedReader(FileReader(file))
            val processName = bufferedReader.readLine().trim { it <= ' ' }
            bufferedReader.close()
            processName
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    private fun getCurrentProcessNameByAms(context: Context): String {
        try {
            val am = ContextCompat.getSystemService(context, ActivityManager::class.java)
                ?: return ""
            val info = am.runningAppProcesses
            if (info == null || info.size == 0) return ""
            val pid = Process.myPid()
            for (aInfo in info) {
                if (aInfo.pid == pid) {
                    if (aInfo.processName != null) {
                        return aInfo.processName
                    }
                }
            }
        } catch (e: Exception) {
            return ""
        }
        return ""
    }

    private fun getCurrentProcessNameByReflect(context: Context): String {
        var processName = ""
        try {
            val app = context.applicationContext as Application
            val loadedApkField = app.javaClass.getField("mLoadedApk")
            loadedApkField.isAccessible = true
            val loadedApk = loadedApkField[app]
            val activityThreadField = loadedApk.javaClass.getDeclaredField("mActivityThread")
            activityThreadField.isAccessible = true
            val activityThread = activityThreadField[loadedApk]
            val getProcessName = activityThread.javaClass.getDeclaredMethod("getProcessName")
            processName = getProcessName.invoke(activityThread) as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return processName
    }
}