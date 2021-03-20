package com.hsicen.library.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * 作者：hsicen  2020/7/11 22:24
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：权限申请处理类
 */
class KPermission private constructor(private val fragmentManager: FragmentManager) {

    /** 权限申请Fragment. */
    private val kPermissionFragment by lazy { PermissionFragment() }

    /** 请求结果. */
    private var onResult: ((isGranted: Boolean, unGranted: Array<String>) -> Unit)? = null

    /**
     * 请求权限.
     * @param permissions Array<String>
     * @param onResult (isGranted: Boolean, revokedPermissions: Array<String>) -> Unit
     */
    fun request(
        permissions: Array<String>,
        onResult: (isGranted: Boolean, revokedPermissions: Array<String>) -> Unit
    ) {
        this.onResult = onResult
        // 这里有可能会申请失败
        runCatching {
            if (!kPermissionFragment.isAdded) {
                fragmentManager.beginTransaction()
                    .add(
                        kPermissionFragment,
                        PermissionFragment::class.java.canonicalName
                            ?: "" + System.currentTimeMillis()
                    ).commitNow()
            }
            kPermissionFragment.requestPermissions(this@KPermission, permissions)
        }.onFailure {
            onResult.invoke(false, permissions)
        }
    }

    /**
     * 权限请求结果处理.
     * @param permissions Array<out String>
     * @param grantResults IntArray
     */
    internal fun onRequestPermissionsResult(
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val granteds = mutableListOf<String>()
        val revokeds = mutableListOf<String>()
        permissions.forEachIndexed { index, s ->
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                granteds.add(s)
            } else {
                revokeds.add(s)
            }
        }
        kPermissionFragment.removeSelf()
        if (revokeds.isEmpty()) {
            onResult?.invoke(true, arrayOf())
        } else {
            onResult?.invoke(false, revokeds.toTypedArray())
        }
    }

    companion object {

        /**
         * 新建一个KPermissions实例.
         * @param target FragmentActivity
         * @return KPermission
         */
        fun with(target: FragmentActivity) = KPermission(target.supportFragmentManager)

        /**
         * 新建一个KPermissions实例.
         * @param target Fragment
         * @return KPermission
         */
        fun with(target: Fragment) = KPermission(target.childFragmentManager)

        /**
         * 是否可以还可以显示权限请求框.
         * @param act Activity
         * @param permissions Array<out String>
         * @return Boolean
         */
        fun shouldShowRequestPermissionRationale(
            act: Activity, vararg permissions: String
        ): Boolean {
            return shouldShowRequestPermissionRationaleM(act, *permissions)
        }

        @TargetApi(Build.VERSION_CODES.M)
        private fun shouldShowRequestPermissionRationaleM(
            act: Activity, vararg permissions: String
        ): Boolean {

            return permissions.any {
                !isGranted(act, it) && act.shouldShowRequestPermissionRationale(it)
            }
        }

        /**
         * 权限是否被允许.
         * @param context Activity
         * @param permission String
         * @return Boolean
         */
        @SuppressLint("WrongConstant")
        fun isGranted(context: Context, permission: String): Boolean {
            return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (context.applicationInfo.targetSdkVersion > Build.VERSION_CODES.M) {
                    ActivityCompat.checkSelfPermission(
                        context, permission
                    ) == PackageManager.PERMISSION_GRANTED
                } else {
                    PermissionChecker.checkSelfPermission(
                        context, permission
                    ) == PackageManager.PERMISSION_GRANTED
                }
            } else {
                ActivityCompat.checkSelfPermission(
                    context, permission
                ) == PackageManager.PERMISSION_GRANTED
            }
        }

        /**
         * 权限是否被拒绝.
         * @param act Activity
         * @param permission String
         * @return Boolean
         */
        fun isRevoked(act: Activity, permission: String): Boolean =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isRevokedM(act, permission)

        @TargetApi(Build.VERSION_CODES.M)
        private fun isRevokedM(act: Activity, permission: String): Boolean {
            return act.packageManager.isPermissionRevokedByPolicy(permission, act.packageName)
        }

        /*** 是否有相册权限*/
        fun isAlbumEnable(context: Context): Boolean {
            return isGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    && isGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}
