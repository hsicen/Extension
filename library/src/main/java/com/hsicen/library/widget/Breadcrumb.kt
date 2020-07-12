package com.hsicen.library.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.hsicen.library.R
import com.hsicen.library.extensions.px2sp

/**
 * <p>作者：Hsicen  2019/7/24 9:24
 * <p>邮箱：codinghuang@163.com
 * <p>作用：
 * <p>描述：Breadcrumb View
 */
class Breadcrumb @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    HorizontalScrollView(context, attrs, defStyleAttr) {

    /*** mark load times*/
    private var mFirstLoad = false
    private val mContainer: LinearLayout
    private var mData = mutableListOf<String>()
    private var mNumBread: Int = 0
    private lateinit var mClickListener: (view: View, index: Int) -> Unit

    private val mSelectColor: Int
    private val mSelectSize: Float
    private val mTextColor: Int
    private val mTextSize: Float
    private val mSplitText: String

    init {
        mFirstLoad = true
        val typedArray = context.resources.obtainAttributes(attrs, R.styleable.Breadcrumb)

        mSelectColor =
            typedArray.getColor(R.styleable.Breadcrumb_selectTextColor, Color.parseColor("#8899a9"))
        // return px value,  if it is sp/dp value, the return value will be (sp/dp * density)
        mSelectSize = typedArray.getDimension(R.styleable.Breadcrumb_selectTextSize, 30f).px2sp
        mTextColor =
            typedArray.getColor(R.styleable.Breadcrumb_normalTextColor, Color.parseColor("#20a6fd"))
        mTextSize = typedArray.getDimension(R.styleable.Breadcrumb_normalTextSize, 30f).px2sp
        mSplitText = typedArray.getString(R.styleable.Breadcrumb_splitMark) ?: ">"
        typedArray.recycle()

        isHorizontalScrollBarEnabled = true
        mContainer = LinearLayout(context)
        mContainer.orientation = LinearLayout.HORIZONTAL
        mContainer.gravity = Gravity.CENTER_VERTICAL
        addView(mContainer)
    }

    /*** add initial data*/
    fun setData(data: MutableList<String>) {
        mData = data
        updateBreadCrumbs()
    }

    /*** add data*/
    fun addData(data: String) {
        mData.add(data)
        updateBreadCrumbs()
    }

    /*** add item click listener*/
    fun addOnItemClick(clickListener: (view: View, index: Int) -> Unit) {
        mClickListener = clickListener
    }

    /*** remove specific index and its after*/
    private fun removeAfterIndex(index: Int) {
        val size = mData.size
        if (index < 0 || index > size) return
        for (i in size - 1 downTo index + 1) mData.removeAt(i)
        updateBreadCrumbs()
    }

    /*** update the breadcrumb's status*/
    private fun updateBreadCrumbs() {
        val size = mData.size

        // add or remove breadcrumbs
        when {
            size > mNumBread -> for (i in mNumBread until size) {
                val itemView =
                    LayoutInflater.from(context).inflate(R.layout.layout_bread_crumb, null)
                val tv = itemView.findViewById(R.id.tv_content) as TextView
                val split = itemView.findViewById(R.id.tv_split) as TextView
                tv.text = mData[i]
                split.text = mSplitText
                tv.setOnClickListener { v ->
                    if (i != mNumBread - 1) mClickListener.invoke(v, i)
                    removeAfterIndex(i)
                }
                addBread(itemView)
            }
            size < mNumBread -> for (i in mNumBread - 1 downTo size - 1 + 1) removeBread(i)
            else -> return
        }

        var start: Int
        if (mFirstLoad) {
            start = 0
            mFirstLoad = false
        } else {
            start = mNumBread - 2
            if (start < 0) start = 0
        }

        //adjust visible range
        for (i in start until mNumBread) {
            val child = mContainer.getChildAt(i)
            highLightIndex(child, i >= mNumBread - 1)
        }

        // scroll to last one
        post { fullScroll(ScrollView.FOCUS_RIGHT) }
    }

    /*** add to container*/
    private fun addBread(view: View) {
        mContainer.addView(view)
        mNumBread = mContainer.childCount
    }

    /*** remove from container*/
    private fun removeBread(i: Int) {
        mContainer.removeViewAt(i)
        mNumBread = mContainer.childCount
    }

    private fun highLightIndex(view: View, highLight: Boolean) {
        val text = view.findViewById(R.id.tv_content) as TextView
        val split = view.findViewById(R.id.tv_split) as TextView

        if (highLight) {
            text.setTextColor(mSelectColor)
            text.textSize = mSelectSize
            split.visibility = View.GONE

        } else {
            text.setTextColor(mTextColor)
            text.textSize = mTextSize
            split.visibility = View.VISIBLE
        }
    }
}
