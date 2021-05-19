package com.hsicen.library.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import java.net.Inet4Address
import java.net.NetworkInterface

/**
 * 作者：hsicen  3/20/21 4:04 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：网络相关扩展
 */


/**
 * 当前网络是否可用.
 */
val Context.networkAvailable: Boolean
    get() = runCatching {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo?.isAvailable == true
    }.getOrNull() ?: false

/**
 * 订阅网络状态.
 * @receiver Context
 * @return LiveData<NetworkInfo.State>
 */
fun Context.liveDataNetworkState(): LiveData<NetworkInfo.State> =
    this.liveDataBroadcast("android.net.conn.CONNECTIVITY_CHANGE")
        .map {
            val connectivityManager =
                this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.activeNetworkInfo?.state ?: NetworkInfo.State.UNKNOWN
        }

/*** 获取当前手机ip地址*/
fun phoneIp(): String {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en?.hasMoreElements() == true) {
            val intf = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    return inetAddress.getHostAddress().toString()
                }
            }
        }
    } catch (ex: Exception) {
        return ""
    }
    return ""
}
