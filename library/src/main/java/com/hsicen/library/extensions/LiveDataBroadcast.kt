package com.hsicen.library.extensions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.MutableLiveData

/**
 * 作者：hsicen  3/20/21 4:03 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：封装广播的LiveData
 */
class LiveDataBroadcast(private val context: Context, private val filters: IntentFilter) :
    MutableLiveData<Intent>() {

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            value = intent
        }
    }

    override fun onActive() {
        super.onActive()
        context.registerReceiver(receiver, filters)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(receiver)
    }
}