package com.hsicen.library.utils

import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

/**
 * 作者：hsicen  3/22/21 5:05 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：ViewGroup tools
 */
object ViewGroupUtils {

    private val sMatrix = ThreadLocal<Matrix>()
    private val sRectF = ThreadLocal<RectF>()

    /**
     * This is a port of the common
     * [ViewGroup.offsetDescendantRectToMyCoords]
     * from the framework, but adapted to take transformations into account. The result
     * will be the bounding rect of the real transformed rect.
     *
     * @param descendant view defining the original coordinate system of rect
     * @param rect       (in/out) the rect to offset from descendant to this view's coordinate system
     */
    fun offsetDescendantRect(parent: ViewGroup, descendant: View, rect: Rect) {
        var m = sMatrix.get()
        if (m == null) {
            m = Matrix()
            sMatrix.set(m)
        } else {
            m.reset()
        }
        offsetDescendantMatrix(parent, descendant, m)
        var rectF = sRectF.get()
        if (rectF == null) {
            rectF = RectF()
            sRectF.set(rectF)
        }
        rectF.set(rect)
        m.mapRect(rectF)
        rect[(rectF.left + 0.5f).toInt(), (rectF.top + 0.5f).toInt(), (rectF.right + 0.5f).toInt()] =
            (rectF.bottom + 0.5f).toInt()
    }

    /**
     * Retrieve the transformed bounding rect of an arbitrary descendant view.
     * This does not need to be a direct child.
     *
     * @param descendant descendant view to reference
     * @param out        rect to set to the bounds of the descendant view
     */
    fun getDescendantRect(parent: ViewGroup, descendant: View, out: Rect) {
        out[0, 0, descendant.width] = descendant.height
        offsetDescendantRect(parent, descendant, out)
    }

    private fun offsetDescendantMatrix(target: ViewParent, view: View, m: Matrix) {
        val parent = view.parent
        if (parent is View && parent !== target) {
            val vp = parent as View
            offsetDescendantMatrix(target, vp, m)
            m.preTranslate(-vp.scrollX.toFloat(), -vp.scrollY.toFloat())
        }

        m.preTranslate(view.left.toFloat(), view.top.toFloat())
        if (!view.matrix.isIdentity) {
            m.preConcat(view.matrix)
        }
    }
}