package com.hsicen.library.log

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * 作者：hsicen  3/20/21 3:55 PM
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：日志工具类
 */
object KLog {

    init {
        val strategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(2)
            .tag("hsc")
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(strategy))
    }

    fun log(priority: Int, tag: String? = null, msg: String? = null, throwable: Throwable? = null) {
        Logger.log(priority, tag, msg, throwable)
    }

    fun d(msg: String, vararg args: Any?) {
        Logger.d(msg, args)
    }

    fun d(msg: Any?) {
        Logger.d(msg)
    }

    fun e(msg: String, vararg args: Any?) {
        Logger.e(msg, args)
    }

    fun e(throwable: Throwable?, msg: String, vararg args: Any?) {
        Logger.e(throwable, msg, args)
    }

    fun i(msg: String, vararg args: Any?) {
        Logger.i(msg, args)
    }

    fun v(msg: String, vararg args: Any?) {
        Logger.v(msg, args)
    }

    fun w(msg: String, vararg args: Any?) {
        Logger.w(msg, args)
    }

    fun wtf(msg: String, vararg args: Any?) {
        Logger.wtf(msg, args)
    }

    fun json(json: String?) {
        Logger.json(json)
    }

    fun xml(json: String?) {
        Logger.xml(json)
    }

}