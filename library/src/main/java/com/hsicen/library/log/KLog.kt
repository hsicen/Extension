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

}