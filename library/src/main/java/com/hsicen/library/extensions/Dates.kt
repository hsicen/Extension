package com.hsicen.library.extensions

import com.orhanobut.logger.Logger
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "hsc: TimeExt"

//日期显示格式
const val FORMAT_CN_YMD = "yyyy年MM月dd日"
const val FORMAT_CN_YMD_NUM = "yyyyMMdd"
const val FORMAT_YEAR_MOUTH_DAT_HOUR_MIN_SECOND = "yyyyMMddHHmmss"
const val FORMAT_MS = "mm:ss"
const val FORMAT_YMD = "yyyy/MM/dd"
const val FORMAT_MDY = "MM/dd/yyyy"
const val FORMAT_DMY = "dd/MM/yyyy"

fun Date.d2s(format: String): String? = formatter(format).format(this)

//各种格式转换成Date
fun Long.l2d() = Date(this)
fun String.s2d(format: String): Date? =
    runCatching {
        formatter(format).parse(this)
    }.onFailure {
        Logger.e("日期转换出错", it)
    }.getOrNull()

//秒转换成Date
fun String?.l2d(isSeconds: Boolean = true): Date? {
    val mills = this?.toLongOrNull() ?: return null
    return Date(mills * if (isSeconds) 1000 else 1)
}

/*** 根据 format 生成对应的 SimpleDateFormat*/
private fun formatter(format: String) = SimpleDateFormat(format, Locale.getDefault())

fun formatNow(format: String): String = formatter(format).format(Date())

/*** 判断两个日期是否为同一个自然周*/
fun sameNatureWeek(start: Long, end: Long): Boolean {
    if (0L == start) return true

    val dateStart = Date(start)
    val dateEnd = Date(end)
    val calenderStart = Calendar.getInstance()
    val calenderEnd = Calendar.getInstance()

    //设置每周的第一天为星期一
    calenderStart.firstDayOfWeek = Calendar.MONDAY
    calenderEnd.firstDayOfWeek = Calendar.MONDAY

    calenderStart.time = dateStart
    calenderEnd.time = dateEnd

    return calenderStart.get(Calendar.WEEK_OF_YEAR) == calenderEnd.get(Calendar.WEEK_OF_YEAR)
}

val Number.secondFormat: String
    get() {
        val formatter = NumberFormat.getInstance().apply {
            maximumIntegerDigits = 2
            minimumIntegerDigits = 2
        }
        val total = toLong()
        val oneMinuteInSeconds = 60L
        val oneHourInSeconds = oneMinuteInSeconds * 60L
        when {
            total >= oneHourInSeconds -> {
                val hours = total / oneHourInSeconds
                val minutes = (total - hours * oneHourInSeconds) / oneMinuteInSeconds
                val seconds = (total - hours * oneHourInSeconds) % oneMinuteInSeconds
                val hoursPart = formatter.format(hours)
                val minutesPart = formatter.format(minutes)
                val secondsPart = formatter.format(seconds)
                return "$hoursPart:$minutesPart:$secondsPart"
            }
            else -> {
                val minutesPart = formatter.format(total / oneMinuteInSeconds)
                val secondsPart = formatter.format(total % oneMinuteInSeconds)
                return "$minutesPart:$secondsPart"
            }
        }
    }
