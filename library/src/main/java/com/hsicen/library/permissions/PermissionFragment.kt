package com.hsicen.library.permissions

import androidx.fragment.app.Fragment

/**
 * 作者：hsicen  2020/7/11 22:24
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：权限申请Fragment，仅用于申请权限.
 */
internal class PermissionFragment : Fragment() {

    /** 请求码. */
    private val requestCode: Int by lazy { START_REQUEST_CODE++ }

    private var kPermissions: KPermission? = null

    /**
     * 请求权限.
     * @param kPermissions KPermission
     * @param permissions Array<String>
     */
    internal fun requestPermissions(kPermissions: KPermission, permissions: Array<String>) {
        this.kPermissions = kPermissions
        requestPermissions(permissions, requestCode)
    }

    /**
     * 移除自己.
     */
    fun removeSelf() {
        fragmentManager?.beginTransaction()?.remove(this)?.commitNowAllowingStateLoss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == this.requestCode && activity != null) {
            kPermissions?.onRequestPermissionsResult(permissions, grantResults)
        } else {
            removeSelf()
        }
    }

    companion object {
        /** 起始请求码. */
        private var START_REQUEST_CODE = 10101
    }
}