package com.hsicen.library.span

import android.annotation.SuppressLint
import android.text.Selection
import android.text.Spannable
import android.text.Spanned
import android.text.method.BaseMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

/**
 * 作者：hsicen  3/20/21 4:12 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：ClickSpan处理
 */
class ClickableMovementMethod : BaseMovementMethod(), View.OnTouchListener {

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {

        val action = event.actionMasked
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {

            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY

            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())

            val link = buffer.getSpans(off, off, ClickableSpan::class.java)
            if (link.isNotEmpty()) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget)
                } else {
                    Selection.setSelection(
                        buffer, buffer.getSpanStart(link[0]),
                        buffer.getSpanEnd(link[0])
                    )
                }
                return true
            } else {
                Selection.removeSelection(buffer)
            }
        }

        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val widget = v as TextView
        val text = widget.text
        if (text is Spanned) {

            val action = event.action

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                var x = event.x.toInt()
                var y = event.y.toInt()

                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop

                x += widget.scrollX
                y += widget.scrollY

                val layout = widget.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())

                val link = text.getSpans(
                    off, off,
                    ClickableSpan::class.java
                )

                if (link.isNotEmpty()) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget)
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        // Selection only works on Spannable text. In our case setSelection doesn't work on spanned text
                        // Selection.setSelection((Spannable) buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    }
                    return true
                }
            }
        }

        return false
    }

    override fun initialize(widget: TextView, text: Spannable) {
        Selection.removeSelection(text)
    }

    companion object {

        /** 单例. */
        val sInstance: ClickableMovementMethod by lazy { ClickableMovementMethod() }
    }
}

/**
 * 使 TextView 的 ClickSpan 可以响应，但是不影响 原TextView其它区域的点击处理，防止父控件无响应
 * @receiver T
 */
fun <T : TextView> T.setClickableMovementMethod() {
    movementMethod = ClickableMovementMethod.sInstance
    setOnTouchListener(ClickableMovementMethod.sInstance)
    isFocusable = false
    isClickable = false
    isLongClickable = false
}