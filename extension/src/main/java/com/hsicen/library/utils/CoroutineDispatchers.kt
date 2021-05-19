package com.hsicen.library.utils

import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 作者：hsicen  3/20/21 4:17 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：协程调度器简单封装
 */
object CoroutinesDispatchers {
    val ui: CoroutineDispatcher = Dispatchers.Main
    val computation: CoroutineDispatcher = Dispatchers.Default
    val io: CoroutineDispatcher = Dispatchers.IO
}

/**
 * 调度到io协程中执行.
 * @param block suspend CoroutineScope.() -> T
 * @return T
 */
suspend fun <T> withIO(block: suspend CoroutineScope.() -> T) =
    withContext(CoroutinesDispatchers.io, block)

/**
 * 调度到ui协程中执行.
 * @param block suspend CoroutineScope.() -> T
 * @return T
 */
suspend fun <T> withUI(block: suspend CoroutineScope.() -> T) =
    withContext(CoroutinesDispatchers.ui, block)

/**
 * 调度到computation协程中执行.
 * @param block suspend CoroutineScope.() -> T
 * @return T
 */
suspend fun <T> withComputation(block: suspend CoroutineScope.() -> T) =
    withContext(CoroutinesDispatchers.computation, block)

/**
 * io协程中执行代码块.
 * @param block suspend CoroutineScope.() -> T
 * @return Job
 */
fun <T> io(block: suspend CoroutineScope.() -> T) = GlobalScope.launch(CoroutinesDispatchers.io) {
    block.invoke(this)
}

/**
 * 主线程中执行代码块.
 * @param block suspend CoroutineScope.() -> T
 * @return Job
 */
fun <T> ui(block: suspend CoroutineScope.() -> T) = GlobalScope.launch(CoroutinesDispatchers.ui) {
    block.invoke(this)
}

/**
 * 计算协程中执行代码块.
 * @param block suspend CoroutineScope.() -> T
 * @return Job
 */
fun <T> computation(block: suspend CoroutineScope.() -> T) =
    GlobalScope.launch(CoroutinesDispatchers.computation) {
        block.invoke(this)
    }

/**
 * io协程中执行代码快.
 * 会自动捕捉异常.
 * @param block suspend CoroutineScope.() -> T
 * @return JobWrapper
 */
fun <T> safetyIO(block: suspend CoroutineScope.() -> T): JobWrapper {
    val jobWrapper = JobWrapper()
    jobWrapper.job = GlobalScope.launch(CoroutinesDispatchers.io) {
        try {
            block.invoke(this)
        } catch (e: Exception) {
            jobWrapper.onFailure?.invoke(e) ?: Logger.e("safetyIO", e)
        }
    }
    return jobWrapper
}

/**
 * 主线程中执行代码块.
 * 会自动捕捉异常.
 * @param block suspend CoroutineScope.() -> T
 * @return JobWrapper
 */
fun <T> safetyUI(block: suspend CoroutineScope.() -> T): JobWrapper {
    val jobWrapper = JobWrapper()
    jobWrapper.job = GlobalScope.launch(CoroutinesDispatchers.ui) {
        try {
            block.invoke(this)
        } catch (e: Exception) {
            jobWrapper.onFailure?.invoke(e) ?: Logger.e("safetyUI", e)
        }
    }
    return jobWrapper
}

/**
 * 计算协程中执行代码块.
 * 会自动捕捉异常.
 * @param block suspend CoroutineScope.() -> T
 * @return JobWrapper
 */
fun <T> safetyComputation(block: suspend CoroutineScope.() -> T): JobWrapper {
    val jobWrapper = JobWrapper()
    jobWrapper.job = GlobalScope.launch(CoroutinesDispatchers.computation) {
        try {
            block.invoke(this)
        } catch (e: Exception) {
            jobWrapper.onFailure?.invoke(e) ?: Logger.e("safetyIO", e)
        }
    }
    return jobWrapper
}

/**
 * 安全的开启一个新协程.
 * @receiver CoroutineScope
 * @param context CoroutineContext
 * @param start CoroutineStart
 * @param block suspend CoroutineScope.() -> Unit
 * @return JobWrapper
 */
fun CoroutineScope.safetyLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): JobWrapper {
    val jobWrapper = JobWrapper()
    jobWrapper.job = launch(context, start) {
        runCatching {
            block.invoke(this)
        }.onFailure {
            jobWrapper.onFailure?.invoke(it) ?: Logger.e("safetyLaunch", it)
        }
    }
    return jobWrapper
}

class JobWrapper {

    lateinit var job: Job

    internal var onFailure: ((Throwable) -> Unit)? = null

    /**
     * 失败回调.
     * @param onFailure (Throwable) -> Unit
     * @return JobWrapper
     */
    fun onFailure(onFailure: (Throwable) -> Unit): JobWrapper {
        this.onFailure = onFailure
        return this
    }
}