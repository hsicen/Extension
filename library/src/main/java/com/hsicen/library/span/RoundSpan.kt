package com.hsicen.library.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan
import com.hsicen.library.dp2px
import com.hsicen.library.sp2px

/**
 * <p>作者：Hsicen  2019/7/23 16:15
 * <p>邮箱：codinghuang@163.com
 * <p>功能：
 * <p>描述：Round rect span shape with spannableString
 */
class RoundSpan(private val bgColor: Int, private val textColor: Int) : ReplacementSpan() {
    private val mRadius = 2f.dp2px
    private var mSize = 0

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {

        mSize = (paint.measureText(text, start, end) + mRadius * 2).toInt()
        return mSize
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val defColor = paint.color
        val def = paint.strokeWidth

        paint.color = bgColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f.dp2px
        paint.isAntiAlias = true

        val mRectF = RectF(
            x + 2.5f,
            y + 2.5f + paint.ascent() - 1f.dp2px,
            x + mSize - 3f.dp2px,
            y + paint.descent() + 1f.dp2px
        )
        canvas.drawRoundRect(mRectF, mRadius, mRadius, paint)

        //draw text
        paint.color = textColor
        paint.style = Paint.Style.FILL
        paint.strokeWidth = def
        paint.textSize = 12f.sp2px
        canvas.drawText(text, start, end, x + mRadius + 3f.dp2px, y.toFloat(), paint)
        paint.color = defColor
    }
}
