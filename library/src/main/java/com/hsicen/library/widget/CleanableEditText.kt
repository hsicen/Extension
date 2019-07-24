package com.hsicen.library.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.textfield.TextInputEditText
import com.hsicen.library.R

/**
 * <p>作者：Hsicen  19-7-24 下午9:59
 * <p>邮箱：codinghuang@163.com
 * <p>作用：
 * <p>描述：A EditText with clear button
 */
class CleanableEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    TextInputEditText(context, attrs, defStyleAttr), View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private val mLeftPadding: Float
    private var mClearIcon: Drawable

    private var mOnFocusChangeListener: View.OnFocusChangeListener? = null
    private var mOnTouchListener: View.OnTouchListener? = null

    init {
        val typedArray = context.resources.obtainAttributes(attrs, R.styleable.CleanableEditText)
        val clearIcon = typedArray.getResourceId(R.styleable.CleanableEditText_clearIcon, R.drawable.icon_clear)
        mLeftPadding = typedArray.getDimension(R.styleable.CleanableEditText_iconLeftPadding, 0f)
        typedArray.recycle()

        mClearIcon = getDrawable(clearIcon)
        mClearIcon.setBounds(0, 0, mClearIcon.intrinsicWidth, mClearIcon.intrinsicHeight)
        setIconVisible(false)

        super.setOnFocusChangeListener(this)
        setOnTouchListener(this)
        addTextChangedListener(this)
    }

    private fun getDrawable(resId: Int): Drawable {
        val drawable = ContextCompat.getDrawable(context, resId)
        return DrawableCompat.wrap(drawable!!)
    }

    private fun setIconVisible(visible: Boolean) {
        mClearIcon.setVisible(visible, false)
        val compoundDrawables = compoundDrawables
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            if (visible) mClearIcon else null,
            compoundDrawables[3]
        )
    }

    /*** set clean icon*/
    fun setClearIcon(resId: Int) {
        mClearIcon = getDrawable(resId)
        setIconVisible(true)
    }

    override fun setOnFocusChangeListener(onFocusChangeListener: View.OnFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener
    }

    override fun setOnTouchListener(onTouchListener: View.OnTouchListener) {
        mOnTouchListener = onTouchListener
    }

    override fun setEnabled(enabled: Boolean) {
        setIconVisible(enabled)
        super.setEnabled(enabled)
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (isFocused) setIconVisible(text?.length ?: 0 > 0)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            setIconVisible(text?.length ?: 0 > 0)
        } else setIconVisible(false)

        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener!!.onFocusChange(v, hasFocus)
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x = event.x.toInt()
        if (mClearIcon.isVisible && x > width - paddingRight - mClearIcon.intrinsicWidth) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                error = null
                setText("")
            }
            return true
        }
        return mOnTouchListener != null && mOnTouchListener!!.onTouch(v, event)
    }

}