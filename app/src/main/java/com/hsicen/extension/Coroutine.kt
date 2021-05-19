package com.hsicen.extension

import android.graphics.Bitmap
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 作者：hsicen  5/3/21 3:34 PM
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：Coroutine
 */

suspend fun bitmapDownload(url: String): Bitmap =
    suspendCoroutine<Bitmap> {
        thread {
            try {
                it.resume(download(url))
            } catch (e: Exception) {
                it.resumeWithException(e)
            }
        }
    }


fun download(url: String): Bitmap {
    TODO("Not yet implemented")
}
