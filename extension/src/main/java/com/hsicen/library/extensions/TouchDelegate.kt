package com.hsicen.library.extensions

import android.graphics.Rect
import android.view.*
import com.hsicen.library.utils.ViewGroupUtils

/**
 * 作者：hsicen  3/22/21 6:18 PM
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：View点击范围扩展处理
 */

class ExpandMultiTouchDelegate(
    bounds: Rect,
    delegateView: View,
    parentView: ViewGroup,
    leftExpand: Int,
    rightExpand: Int,
    topExpand: Int,
    bottomExpand: Int
) : TouchDelegate(bounds, delegateView) {

    constructor(bounds: Rect, delegateView: View, parentView: ViewGroup, expand: Int) :
            this(bounds, delegateView, parentView, expand, expand, expand, expand)

    private var mDelegateTargeted = false
    private val mBounds = bounds
    private val mSlop = ViewConfiguration.get(delegateView.context).scaledTouchSlop
    private val mSlopBounds = Rect(bounds)
    private val mDelegateView = delegateView
    private val mParentView = parentView
    private val mLeftExpand = leftExpand
    private val mRightExpand = rightExpand
    private val mTopExpand = topExpand
    private val mBottomExpand = bottomExpand
    private val mChildInParentBounds = Rect()

    init {
        mSlopBounds.inset(-mSlop, -mSlop)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        var sendToDelegate = false
        var hit = true
        var handled = false

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mDelegateTargeted = mBounds.contains(x, y)
                sendToDelegate = mDelegateTargeted
            }

            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_MOVE -> {
                sendToDelegate = mDelegateTargeted
                if (sendToDelegate) {
                    val slopBounds = mSlopBounds
                    if (!slopBounds.contains(x, y)) {
                        hit = false
                    }
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                sendToDelegate = mDelegateTargeted
                mDelegateTargeted = false
            }
        }

        if (sendToDelegate) {
            val delegateView = mDelegateView
            if (hit) {
                //主要修改在这，针对加减购布局，加购和减购按钮特殊处理
                if (delegateView is ViewGroup) {
                    val count = delegateView.childCount
                    for (i in 0 until count) {
                        if (i != 0 && i != count - 1) continue
                        val child = delegateView.getChildAt(i)
                        ViewGroupUtils.getDescendantRect(mParentView, child, mChildInParentBounds)
                        mChildInParentBounds.top -= mTopExpand
                        mChildInParentBounds.bottom += mBottomExpand
                        mChildInParentBounds.left -= mLeftExpand
                        mChildInParentBounds.right += mRightExpand

                        if (mChildInParentBounds.contains(x, y)) {
                            event.setLocation((child.width / 2f), (child.height / 2f))
                            return child.dispatchTouchEvent(event)
                        }
                    }
                    return false
                } else {
                    event.setLocation((delegateView.width / 2f), (delegateView.height / 2f))
                }
            } else {
                val slop = mSlop
                event.setLocation(-(slop * 2).toFloat(), -(slop * 2).toFloat())
            }
            handled = delegateView.dispatchTouchEvent(event)
        }

        return handled
    }
}

/**
 *扩大View的点击范围,支出多个View的范围点击设置
 * @param parentView 具有多个点击控件的View
 * @param width 要扩大的尺寸，向View四周延伸
 */
inline fun View.enlargeMultiClickBounds(parentView: ViewGroup, width: Int) {
    post {
        val bounds = Rect()
        ViewGroupUtils.getDescendantRect(parentView, this, bounds)
        bounds.inset(-width, -width)

        parentView.touchDelegate = ExpandMultiTouchDelegate(bounds, this, parentView, width)
    }
}

/**
 *扩大View的点击范围,支出多个View的范围点击设置
 * @param parentView 具有多个点击控件的View
 * @param leftExpand 要扩大的尺寸
 * @param topExpand 要扩大的尺寸
 * @param rightExpand 要扩大的尺寸
 * @param bottomExpand 要扩大的尺寸
 */
inline fun View.enlargeMultiClickBounds(
    parentView: ViewGroup,
    leftExpand: Int,
    topExpand: Int,
    rightExpand: Int,
    bottomExpand: Int
) {
    post {
        val bounds = Rect()
        ViewGroupUtils.getDescendantRect(parentView, this, bounds)
        bounds.left -= leftExpand
        bounds.top -= topExpand
        bounds.right += rightExpand
        bounds.bottom += bottomExpand

        parentView.touchDelegate = ExpandMultiTouchDelegate(
            bounds,
            this,
            parentView,
            leftExpand,
            rightExpand,
            topExpand,
            bottomExpand
        )
    }
}

/**
 * 扩大View的点击范围
 *
 * @param parent 要扩大点击范围View的父View,如果扩大的点击区域大于父View，则为能提供大小的父ViewGroup
 * @param width 要扩大的尺寸，向View四周延伸
 */
inline fun View.enlargeSingleClickBounds(parent: ViewGroup, width: Int) {
    post {
        val rect = Rect()
        ViewGroupUtils.getDescendantRect(parent, this, rect)

        rect.inset(-width, -width)
        parent.touchDelegate = TouchDelegate(rect, this)
    }
}



