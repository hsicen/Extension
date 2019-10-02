package com.hsicen.library.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager

/**
 * <p>作者：Hsicen  2019/10/2 17:27
 * <p>邮箱：codinghuang@163.com
 * <p>功能：
 * <p>描述：禁止vp左右滑动
 */
class NoScrollViewPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    /*** 默认禁止滑动*/
    private var noScroll = false

    fun setNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return this.noScroll && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return this.noScroll && super.onInterceptTouchEvent(event)
    }

}
