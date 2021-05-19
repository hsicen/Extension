package com.hsicen.library.extensions

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.view.*
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orhanobut.logger.Logger


/**
 * 点击事件.
 * @receiver View
 * @param block (View) -> Unit
 */
inline fun View.click(noinline block: (View) -> Unit) {
    this.setOnClickListener(block)
}

/**
 * 点击事件防重复.
 * @receiver View
 * @param duration Long
 * @param block (View) -> Unit
 */
inline fun View.clickThrottle(duration: Long = 500L, crossinline block: (View) -> Unit) {
    // 最新的点击时间.
    var lastClickTime = 0L
    this.setOnClickListener {
        if (System.currentTimeMillis() - lastClickTime > duration) {
            lastClickTime = System.currentTimeMillis()
            block.invoke(it)
        }
    }
}

/**
 * 点击事件防重复.
 * @receiver Array<View>
 * @param duration Long
 * @param block (View) -> Unit
 */
inline fun Array<View>.clickThrottle(duration: Long = 500L, crossinline block: (View) -> Unit) {
    // 最新的点击时间.
    var lastClickTime = 0L
    this.forEach { v ->
        v.click {
            if (System.currentTimeMillis() - lastClickTime > duration) {
                lastClickTime = System.currentTimeMillis()
                block.invoke(it)
            }
        }
    }
}

operator fun View.plus(view: View): Array<View> = arrayOf(this, view)

/**
 *  @param isInflater 是否是inflater出来的.
 */
fun View.toBitmap(isInflater: Boolean = false): Bitmap? {
    return kotlin.runCatching {

        val width = if (this.width <= 0) screenWidth() else this.width
        val height = if (this.height <= 0) screenHeight() else this.height

        if (isInflater) {
            val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            this.measure(measuredWidth, measuredHeight)
            this.layout(0, 0, this.measuredWidth, this.measuredHeight)
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // canvas.drawColor(Color.WHITE)
        this.draw(canvas)
        bitmap
    }.onFailure {
        Logger.e("view", "error", it)
    }.getOrNull()
}

/**
 * 禁止滑动关闭.
 * @receiver BottomSheetDialogFragment
 */
inline fun BottomSheetDialogFragment.forbidScroll() {
    view?.viewTreeObserver?.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            view?.viewTreeObserver?.removeGlobalOnLayoutListener(this)
            BottomSheetBehavior.from(view?.parent as View).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            this@apply.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }
                })
            }
        }
    })
}

fun View.updateMargin(
    layout: ViewGroup.MarginLayoutParams = (layoutParams as ViewGroup.MarginLayoutParams),
    left: Int = layout.leftMargin,
    top: Int = layout.topMargin,
    right: Int = layout.rightMargin,
    bottom: Int = layout.bottomMargin
) {
    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(left, top, right, bottom)
}

inline fun View.onWindowInsetsApply(crossinline apply: (v: View, left: Int, top: Int, right: Int, bottom: Int) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val cutout = insets.displayCutout
        if (cutout != null) {
            apply(
                v,
                cutout.safeInsetLeft,
                cutout.safeInsetTop,
                cutout.safeInsetRight,
                cutout.safeInsetBottom
            )
        } else {
            apply(v, 0, 0, 0, 0)
        }
        insets.consumeSystemWindowInsets()
    }
}

inline fun View.show() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

inline fun View.hide(full: Boolean = true) {
    if (full && visibility != View.GONE) {
        visibility = View.GONE
    } else if (!full && visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

@SuppressLint("ClickableViewAccessibility")
fun View.setCustomClickListener(
    longClickDuration: Long = 500L,
    onClick: ((v: View) -> Unit)? = null,
    onLongClick: ((v: View) -> Boolean)? = null
) {
    var clickHandled = false

    if (onClick != null && !isClickable) isClickable = true
    if (onLongClick != null && !isLongClickable) isLongClickable = true
    if (!isClickable && !isLongClickable) return

    val clickAction = Runnable {
        if (onClick != null && isClickable) {
            onClick.invoke(this)
            playSoundEffect(SoundEffectConstants.CLICK)
        }
    }
    val longClickAction = Runnable {
        if (onLongClick != null && isLongClickable) {
            clickHandled = onLongClick.invoke(this)
            if (clickHandled) {
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            }
        }
    }

    fun View.touchIn(rawX: Float, rawY: Float): Boolean {
        val location = IntArray(2)
        getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + width
        val bottom = top + height
        return rawX >= left && rawX <= right && rawY >= top && rawY <= bottom
    }

    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            clickHandled = false
            v.removeCallbacks(longClickAction)
            v.postDelayed(longClickAction, longClickDuration)
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            if (!touchIn(event.rawX, event.rawY)) {
                v.removeCallbacks(longClickAction)
            }
        } else if (event.action == MotionEvent.ACTION_UP) {
            if (!clickHandled && touchIn(event.rawX, event.rawY)) {
                clickAction.run()
            }
            v.removeCallbacks(longClickAction)
        } else if (event.action == MotionEvent.ACTION_CANCEL) {
            v.removeCallbacks(longClickAction)
        }
        true
    }
}

/*** 改变字重*/
inline fun TextView.changeTextStyle(bold: Boolean) {
    setTypeface(null, if (bold) Typeface.BOLD else Typeface.NORMAL)
}

/**
 * 尝试设置字重为 Medium
 *
 * !!! not work as expected
 * @param[fallbackToBold] 没有 Medium 字重时回滚到 Bold 还是 Normal
 */
inline fun TextView.tryMediumWeight(fallbackToBold: Boolean = false) {
    val medium = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        val tf = Typeface.create(Typeface.DEFAULT, 500, false)
        if (tf == Typeface.DEFAULT) null else tf
    } else null

    if (medium == null) {
        setTypeface(null, if (fallbackToBold) Typeface.BOLD else Typeface.NORMAL)
    } else {
        typeface = medium
    }
}

inline val View.layoutInflater: LayoutInflater inline get() = LayoutInflater.from(context)
