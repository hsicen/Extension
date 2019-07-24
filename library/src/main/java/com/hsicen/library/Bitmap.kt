package com.hsicen.library

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

/**
 * <p>作者：Hsicen  2019/7/23 15:22
 * <p>邮箱：codinghuang@163.com
 * <p>作用：
 * <p>描述：Bitmap extension
 */


/** *  Resize bitmap */
fun Bitmap.resize(width: Int, height: Int) = Bitmap.createScaledBitmap(this, width, height, true)


/***  Bitmap to drawable*/
fun Bitmap.toDrawable() = BitmapDrawable(Resources.getSystem(), this)


/*** resource file to bitmap
 * it works for xml type drawable
 * use canvas to drawable a bitmap
 */
fun drawable2Bitmap(drawable: Drawable): Bitmap {

    if (drawable is BitmapDrawable) {
        if (drawable.bitmap != null) return drawable.bitmap
    }

    val mBitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    }

    val canvas = Canvas(mBitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)

    return mBitmap
}


/*** resource file to Bitmap
 *  it  works for jpg and png type drawables, but it does not work for xml type drawable
 *  I guess because xml file has no specific width and height information
 * */
fun res2Bitmap(resId: Int) = BitmapFactory.decodeResource(Resources.getSystem(), resId)


/***  convert drawable 2 bitmap with specific width*/
fun res2Bitmap(resId: Int, width: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(Resources.getSystem(), resId, options)
    options.inJustDecodeBounds = false
    options.inDensity = options.outWidth
    options.inTargetDensity = width

    return BitmapFactory.decodeResource(Resources.getSystem(), resId, options)
}


/*** get bitmap from assets with file name*/
fun getBitmapFromAssets(fileName: String): Bitmap {
    val assetManager = Resources.getSystem().assets
    val stream = assetManager.open(fileName)
    val decodeBitmap = BitmapFactory.decodeStream(stream)
    stream.close()

    return decodeBitmap
}


/*** scale source bitmap with sample bitmap in 1080p screen*/
fun scaleBitmap(source: Bitmap, sample: Bitmap): Bitmap {
    val scale = sample.width / 1080f
    val matrix = Matrix()
    matrix.postScale(scale, scale)

    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}


/** composite two bitmap
 *   src is the bottom layer  upLayer is on the top
 */
fun Bitmap.composite(upLayer: Bitmap, padLeft: Float, padTop: Float): Bitmap {
    val newBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(newBitmap)
    canvas.drawBitmap(this, 0f, 0f, null)
    canvas.drawBitmap(upLayer, padLeft, padTop, null)
    canvas.save()
    canvas.restore()

    return newBitmap
}


/*** save bitmap with specific directory*/
fun Bitmap.save(childPath: String): String {
    val bitmapDir = File(Environment.getExternalStorageDirectory(), childPath)
    if (!bitmapDir.exists()) bitmapDir.mkdir()

    val fileName = System.currentTimeMillis().toString() + ".jpg"
    val file = File(bitmapDir, fileName)

    val fos = FileOutputStream(file)
    this.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    fos.flush()
    fos.close()

    val path = file.absolutePath
    this.recycle()

    return path
}
