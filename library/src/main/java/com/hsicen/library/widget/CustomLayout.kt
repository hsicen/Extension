@file:Suppress("MemberVisibilityCanBePrivate")

package com.hsicen.library.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * 作者：hsicen  3/21/21 10:50 AM
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：自定义ViewGroup基类
 *
 * measuredWidth：是在measure中赋值的
 * width：是在layout或onLayout中赋值的
 * 在onDraw中尽量使用width参数
 */
abstract class CustomLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    protected fun View.autoMeasure() {
        measure(
            this.defaultWidthMeasureSpec(this@CustomLayout),
            this.defaultHeightMeasureSpec(this@CustomLayout)
        )
    }

    protected fun View.layout(x: Int, y: Int, fromRight: Boolean = false) {
        if (fromRight) {
            layout(this@CustomLayout.measuredWidth - measuredWidth - x, y)
        } else {
            layout(x, y, x + measuredWidth, y + measuredHeight)
        }
    }

    protected val View.measuredWidthWithMargins
        get() = (measuredWidth)


    protected val View.measuredHeightWithMargins
        get() = (measuredHeight)

    protected fun View.defaultWidthMeasureSpec(parentView: ViewGroup): Int {
        return when (layoutParams.width) {
            ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> parentView.measuredWidth.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.width.toExactlyMeasureSpec()
        }
    }

    protected fun View.defaultHeightMeasureSpec(parentView: ViewGroup): Int {
        return when (layoutParams.height) {
            ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredHeight.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> parentView.measuredHeight.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.height.toExactlyMeasureSpec()
        }
    }

    protected fun Int.toExactlyMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY)
    }

    protected fun Int.toAtMostMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST)
    }

    protected class LayoutParams(width: Int, height: Int) : MarginLayoutParams(width, height)
}
