@file:Suppress("NOTHING_TO_INLINE")

package com.hsicen.library.extensions

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorInflater
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.core.app.NotificationManagerCompat
import com.hsicen.library.utils.AppContextHolder
import com.hsicen.library.utils.ProcessUtils

/**
 * 作者：hsicen  3/20/21 3:36 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：上下文对象功能扩展
 */

inline val Context.appName
    inline get() = appName(packageName)

inline fun Context.appName(packageName: String) =
    packageManager.getPackageInfo(
        packageName,
        0
    )?.applicationInfo?.loadLabel(packageManager).toString()

inline val Context.appIcon
    inline get() = appIcon(packageName)

inline fun Context.appIcon(packageName: String) =
    packageManager.getPackageInfo(packageName, 0)?.applicationInfo?.loadIcon(packageManager)

val versionName by lazy {
    AppContextHolder.mContext.packageManager.getPackageInfo(
        AppContextHolder.mContext.packageName,
        0
    )?.versionName ?: ""
}

val versionCode by lazy {
    val context = AppContextHolder.mContext
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        context.packageManager.getPackageInfo(context.packageName, 0)?.longVersionCode ?: 0
    } else {
        context.packageManager.getPackageInfo(context.packageName, 0)?.versionCode ?: 0
    }
}


/*** 判断通知权限是否开启.*/
inline fun Context.notificationEnable() =
    NotificationManagerCompat.from(this).areNotificationsEnabled()

/**
 * 调用系统浏览器.
 * @receiver Context
 * @param url String
 * @param newTask Boolean 是否添加FLAG_ACTIVITY_NEW_TASK
 * @return Boolean
 */
inline fun Context.browse(url: String, newTask: Boolean = false): Boolean =
    kotlin.runCatching {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (newTask) {
            intent.newTask()
        }
        startActivity(intent)
        true
    }.getOrNull() ?: false

/*** 获取状态栏高度*/
inline val Context.statusBarHeight
    inline get() = resources.getDimensionPixelSize(
        Resources.getSystem().getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
    )

/*** 获取导航栏高度*/
inline val Context.actionbarHeight: Int
    inline get() {
        val ta = obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val value = ta.getDimensionPixelSize(ta.getIndex(0), 0)
        ta.recycle()
        return value
    }

/**
 * 加载布局文件.
 * @receiver Context
 * @param resId Int
 * @param parent ViewGroup?
 * @param attachToRoot Boolean
 * @return View
 */
inline fun Context.inflateLayout(
    resId: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = parent != null
): View =
    LayoutInflater.from(this).inflate(resId, parent, attachToRoot)

/**
 * 调用系统拨号.
 * @receiver Context
 * @param number String
 * @return Boolean
 */
@RequiresPermission(Manifest.permission.CALL_PHONE)
inline fun Context.makeCall(number: String): Boolean =
    kotlin.runCatching {
        startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")).newTask())
        true
    }.onFailure {
        it.printStackTrace()
    }.getOrNull() ?: false

/**
 * 跳转到系统拨号页面.
 * @receiver Context
 * @param number String
 * @return Boolean
 */
inline fun Context.makeDial(number: String): Boolean =
    kotlin.runCatching {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")).newTask())
        true
    }.onFailure {
        it.printStackTrace()
    }.getOrNull() ?: false


/*** 将字符串复制到粘贴板*/
inline fun Context.setClipboardContent(content: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(null, content))
}

inline fun Context.setClipboardContent(@StringRes resId: Int) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(null, string(resId)))
}

/*** 获取粘贴板的字符串*/
inline fun Context.getClipboardContent(): String {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val primaryClip = clipboard.primaryClip
    if (primaryClip != null && primaryClip.itemCount > 0) {
        return primaryClip.getItemAt(0).text.toString()
    }
    return ""
}

/*** 跳转手机设置页面*/
inline fun Context.toPhoneSetting() {
    val intent = Intent(Settings.ACTION_SETTINGS)
    startActivity(intent)
}

/*** 跳转运营商设置页面*/
inline fun Context.toNetworkSetting() {
    val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
    startActivity(intent)
}

/*** 跳转无线网设置页面*/
inline fun Context.toWifiSetting() {
    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
    startActivity(intent)
}

/*** 跳转应用详情界面*/
inline fun Context.toAppDetails() {
    val intent = Intent()
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
    intent.data = Uri.fromParts("package", packageName, null)
    startActivity(intent)
}

inline fun Context.inflateTransition(@TransitionRes resId: Int): Transition {
    return TransitionInflater.from(this).inflateTransition(resId)
}

inline fun Context.loadAnimator(@AnimatorRes resId: Int): Animator {
    return AnimatorInflater.loadAnimator(this, resId)
}

inline fun Context.loadAnimation(@AnimRes resId: Int): Animation {
    return AnimationUtils.loadAnimation(this, resId)
}

inline val Context.viewConfiguration: ViewConfiguration
    inline get() = ViewConfiguration.get(this)

inline val Context.currentProcessName: String
    inline get() = ProcessUtils.getCurrentProcessName(this)

inline val Context.isMainProcess: Boolean
    inline get() = ProcessUtils.isMainProcess(this)

inline fun <reified T : Activity> Context.startActivity(
    options: Bundle? = null,
    data: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java).apply(data)
    startActivity(intent, options)
}

inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    options: Bundle? = null,
    data: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java).apply(data)
    startActivityForResult(intent, requestCode, options)
}

inline fun Activity.setResult(resultCode: Int, data: Intent.() -> Unit) {
    setResult(resultCode, Intent().apply(data))
}
