package com.hsicen.library.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment

/**
 * 作者：hsicen  3/20/21 4:03 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：键盘操作工具类
 */

/**
 * 打开软键盘
 * @receiver EditText
 */
fun EditText.showKeyboard() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}

/**
 * 延迟弹出键盘
 */
fun EditText.showKeyboardWithDelay(@IntRange(from = 0) delayTime: Long = 0) {
    postDelayed(::showKeyboard, delayTime)
}

/**
 * 关闭软键盘.
 * @receiver View
 * @param delayTime Long
 */
fun View.hideKeyboardWithDelay(@IntRange(from = 0) delayTime: Long = 0) {
    postDelayed({
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }, delayTime)
}

/**
 * 关闭软键盘.
 * @receiver Activity
 */
fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive && currentFocus != null) {
        currentFocus?.windowToken?.let {
            imm.hideSoftInputFromWindow(it, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}


/*** 立即关闭软键盘*/
fun Activity.hideKeyboardNow() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

/**
 * 关闭软键盘.
 * @receiver Fragment
 */
fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun Fragment.hideKeyboardNow() {
    activity?.hideKeyboardNow()
}

/**
 * 点击非 EditText 区域， 关闭软键盘.
 * @receiver Activity
 * @param event MotionEvent
 * @param view View?
 */
fun Activity.hideKeyBoardOnTouch(event: MotionEvent, view: View?) {
    runCatching {
        if (view != null && view is EditText) {
            val location = intArrayOf(0, 0)
            view.getLocationInWindow(location)
            val left = location[0]
            val top = location[1]
            val right = left + view.width
            val bottom = top + view.height
            if (event.rawX < left || event.rawX > right || event.rawY < top || event.rawY > bottom) {
                this.hideKeyboard()
                view.clearFocus()
            }
        }
    }
}

/**
 * 监听键盘是收起还是展开状态.
 * @receiver Activity
 * @param onShow ((height: Int) -> Unit)?
 * @param onHide ((height: Int) -> Unit)?
 */
fun Activity.setSoftKeyBoardListener(
    onShow: ((height: Int) -> Unit)? = null,
    onHide: ((height: Int) -> Unit)? = null
) {
    // 记录根视图的显示高度
    var rootViewVisibleHeight = 0
    // 获取activity的根视图
    val rootView = window.decorView
    // 监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
    rootView.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
        // 获取当前根视图在屏幕上显示的大小
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val visibleHeight = r.height()
        if (rootViewVisibleHeight == 0) {
            rootViewVisibleHeight = visibleHeight
            return@OnGlobalLayoutListener
        }
        // 根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
        if (rootViewVisibleHeight == visibleHeight) {
            return@OnGlobalLayoutListener
        }
        // 根视图显示高度变小超过200，可以看作软键盘显示了
        if (rootViewVisibleHeight - visibleHeight > 200) {
            onShow?.invoke(rootViewVisibleHeight - visibleHeight)
            rootViewVisibleHeight = visibleHeight
            return@OnGlobalLayoutListener
        }
        // 根视图显示高度变大超过200，可以看作软键盘隐藏了
        if (visibleHeight - rootViewVisibleHeight > 200) {
            onHide?.invoke(visibleHeight - rootViewVisibleHeight)
            rootViewVisibleHeight = visibleHeight
            return@OnGlobalLayoutListener
        }
    })
}
