package com.hsicen.library.widget

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView

/**
 * 作者：hsicen  4/7/21 11:25 AM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：ImageView高斯模糊处理
 * 结合Glide使用
 */
class BlurImageView : ShapeableImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    fun setGaussBlur(radius: Int, source: Bitmap): Bitmap {
        val renderScript = RenderScript.create(context)
        val input = Allocation.createFromBitmap(renderScript, source)
        val output = Allocation.createTyped(renderScript, input.type)
        val scriptIntrinsicBlur: ScriptIntrinsicBlur =
            ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        scriptIntrinsicBlur.setInput(input)
        scriptIntrinsicBlur.setRadius(radius.toFloat())
        scriptIntrinsicBlur.forEach(output)
        output.copyTo(source)
        renderScript.destroy()
        return source
    }

    /*fun setGaussBlur(): RequestOptions {
        return RequestOptions.bitmapTransform(GlideBlurTransformation(AppContextHolder.mContext))
    }*/

    companion object {
        fun blurBitmap(
            context: Context, source: Bitmap, blurRadius: Float,
            outWidth: Int, outHeight: Int
        ): Bitmap {
            val inputBitmap = Bitmap.createScaledBitmap(source, outWidth, outHeight, false)
            val outputBitmap = Bitmap.createBitmap(inputBitmap)
            val rs = RenderScript.create(context)
            val blurScript: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
            blurScript.setRadius(blurRadius)
            blurScript.setInput(tmpIn)
            blurScript.forEach(tmpOut)
            tmpOut.copyTo(outputBitmap)
            inputBitmap.recycle()
            blurScript.destroy()
            rs.destroy()
            return outputBitmap
        }

        fun Int.roundTo1(): Int {
            return if (this <= 0) {
                1
            } else this
        }
    }
}

/*class GlideBlurTransformation internal constructor(private val context: Context) :
    CenterCrop() {
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val bitmap = super.transform(pool, toTransform, outWidth, outHeight)
        return blurBitmap(
            context,
            bitmap,
            20f,
            (outWidth * 0.2).toInt().roundTo1(),
            (outHeight * 0.2).toInt().roundTo1()
        )
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}*/


