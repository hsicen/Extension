package com.hsicen.library.span

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.orhanobut.logger.Logger
import java.lang.ref.WeakReference

/**
 * 作者：hsicen  3/20/21 4:11 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：Span工具类
 */
class Span internal constructor(private val ctx: Context) {

    private val builder = SpannableStringBuilder()
    private var text: CharSequence = ""

    private var flag: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    private var foregroundColor: Int = COLOR_DEFAULT
    private var backgroundColor: Int = COLOR_DEFAULT
    private var lineHeight: Int = -1
    private var alignLine: Int = 0
    private var quoteColor: Int = COLOR_DEFAULT
    private var stripeWidth: Int = 0
    private var quoteGapWidth: Int = 0
    private var first: Int = -1
    private var rest: Int = 0
    private var bulletColor: Int = COLOR_DEFAULT
    private var bulletRadius: Int = 0
    private var bulletGapWidth: Int = 0
    private var fontSize: Int = -1
    private var fontSizeIsDp: Boolean = false
    private var proportion: Float = -1f
    private var xProportion: Float = -1f
    private var isStrikethrough: Boolean = false
    private var isUnderline: Boolean = false
    private var isSuperscript: Boolean = false
    private var isSubscript: Boolean = false
    private var isBold: Boolean = false
    private var isItalic: Boolean = false
    private var isBoldItalic: Boolean = false
    private var fontFamily: String? = null

    private var typeface: Typeface? = null
    private var alignment: Layout.Alignment? = null
    private var clickSpan: ClickableSpan? = null
    private var url: String? = null
    private var blurRadius: Float = -1f
    private var style: BlurMaskFilter.Blur? = null
    private var shader: Shader? = null
    private var shadowRadius: Float = -1f
    private var shadowDx: Float = 0f
    private var shadowDy: Float = 0f
    private var shadowColor: Int = COLOR_DEFAULT
    private var spans: Array<out Any>? = null

    private var imageBitmap: Bitmap? = null
    private var imageDrawable: Drawable? = null
    private var imageUri: Uri? = null
    private var imageResourceId: Int = -1
    private var alignImage: Int = 0

    private var spaceSize: Int = -1
    private var spaceColor: Int = COLOR_DEFAULT

    private var type: Int = 0
    private var typeCharSequence = 0
    private var typeImage = 1
    private var typeSpace = 2

    /**
     * 重置所有参数.
     */
    private fun reset() {
        flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        foregroundColor = COLOR_DEFAULT
        backgroundColor = COLOR_DEFAULT
        lineHeight = -1
        quoteColor = COLOR_DEFAULT
        first = -1
        bulletColor = COLOR_DEFAULT
        fontSize = -1
        proportion = -1f
        xProportion = -1f
        isStrikethrough = false
        isUnderline = false
        isSuperscript = false
        isSubscript = false
        isBold = false
        isItalic = false
        isBoldItalic = false
        fontFamily = null
        typeface = null
        alignment = null
        clickSpan = null
        url = null
        blurRadius = -1f
        shader = null
        shadowRadius = -1f
        spans = null

        imageBitmap = null
        imageDrawable = null
        imageUri = null
        imageResourceId = -1

        spaceSize = -1
    }

    fun flag(flag: Int) {
        this.flag = flag
    }

    fun foregroundColor(@ColorInt color: Int) {
        this.foregroundColor = color
    }

    fun backgroundColor(@ColorInt color: Int) {
        this.backgroundColor = color
    }

    fun lineHeight(@IntRange(from = 0L) lineHeight: Int, @Align align: Int = ALIGN_CENTER) {
        this.lineHeight = lineHeight
        this.alignLine = align
    }

    fun quoteColor(
        @ColorInt color: Int,
        @IntRange(from = 1) stripeWidth: Int = 2,
        @IntRange(from = 0L) gapWidth: Int = 2
    ) {
        this.quoteColor = color
        this.stripeWidth = stripeWidth
        this.quoteGapWidth = gapWidth
    }

    fun leadingMargin(@IntRange(from = 0L) first: Int, @IntRange(from = 0L) rest: Int) {
        this.first = first
        this.rest = rest
    }

    fun bullet(
        @ColorInt color: Int = 0,
        @IntRange(from = 0L) radius: Int = 3,
        @IntRange(from = 0L) gapWidth: Int
    ) {
        this.bulletColor = color
        this.bulletRadius = radius
        this.bulletGapWidth = gapWidth
    }

    fun fontSize(@IntRange(from = 0L) size: Int, isSp: Boolean = false) {
        this.fontSize = size
        this.fontSizeIsDp = isSp
    }

    fun fontProportion(proportion: Float) {
        this.proportion = proportion
    }

    fun fontXProportion(proportion: Float) {
        this.xProportion = proportion
    }

    fun strikethrough() {
        this.isStrikethrough = true
    }

    fun underline() {
        this.isUnderline = true
    }

    fun superscript() {
        this.isSuperscript = true
    }

    fun subscript() {
        this.isSubscript = true
    }

    fun bold() {
        this.isBold = true
    }

    fun boldItalic() {
        this.isBoldItalic = true
    }

    fun fontFamily(fontFamily: String) {
        this.fontFamily = fontFamily
    }

    fun typeface(typeface: Typeface) {
        this.typeface = typeface
    }

    fun align(alignment: Layout.Alignment) {
        this.alignment = alignment
    }

    fun clickSpan(clickSpan: ClickableSpan) {
        this.clickSpan = clickSpan
    }

    fun url(url: String) {
        this.url = url
    }

    fun blur(
        @FloatRange(from = 0.0, fromInclusive = false) radius: Float,
        style: BlurMaskFilter.Blur
    ) {
        this.blurRadius = radius
        this.style = style
    }

    fun shader(shader: Shader) {
        this.shader = shader
    }

    fun shadow(
        @FloatRange(from = 0.0, fromInclusive = false) radius: Float,
        dx: Float,
        dy: Float,
        shadowColor: Int
    ) {
        this.shadowRadius = radius
        this.shadowDx = dx
        this.shadowDy = dy
        this.shadowColor = shadowColor
    }

    fun spans(vararg span: Any) {
        if (span.isNotEmpty()) {
            this.spans = span
        }
    }

    fun append(text: CharSequence) {
        apply(typeCharSequence)
        this.text = text
    }

    fun appendLine() {
        apply(typeCharSequence)
        text = LINE_SEPARATOR
    }

    fun appendLine(text: CharSequence) {
        apply(typeCharSequence)
        this.text = text.toString() + LINE_SEPARATOR
    }

    fun appendImage(bitmap: Bitmap, @Align align: Int = ALIGN_CENTER) {
        apply(typeImage)
        this.imageBitmap = bitmap
        this.alignImage = align
    }

    fun appendImage(drawable: Drawable, @Align align: Int = ALIGN_CENTER) {
        apply(typeImage)
        this.imageDrawable = drawable
        this.alignImage = align
    }

    fun appendImage(uri: Uri, @Align align: Int = ALIGN_CENTER) {
        apply(typeImage)
        this.imageUri = uri
        this.alignImage = align
    }

    fun appendImage(@DrawableRes resourceId: Int, @Align align: Int = ALIGN_CENTER) {
        // it's important for span start with image
        append(Character.toString(0.toChar()))
        apply(typeImage)
        this.imageResourceId = resourceId
        this.alignImage = align
    }

    fun appendSpace(@IntRange(from = 0) size: Int, @ColorInt color: Int = Color.TRANSPARENT) {
        apply(typeSpace)
        spaceSize = size
        spaceColor = color
    }

    private fun apply(type: Int) {
        applyLast()
        this.type = type
    }

    private fun applyLast() {
        when (type) {
            typeCharSequence -> updateCharCharSequence()
            typeImage -> updateImage()
            typeSpace -> updateSpace()
        }
        reset()
    }

    private fun updateCharCharSequence() {
        if (text.isEmpty()) return
        val start = builder.length
        builder.append(text)
        val end = builder.length
        if (foregroundColor != COLOR_DEFAULT) {
            builder.setSpan(ForegroundColorSpan(foregroundColor), start, end, flag)
        }
        if (backgroundColor != COLOR_DEFAULT) {
            builder.setSpan(BackgroundColorSpan(backgroundColor), start, end, flag)
        }
        if (first != -1) {
            builder.setSpan(LeadingMarginSpan.Standard(first, rest), start, end, flag)
        }
        if (quoteColor != COLOR_DEFAULT) {
            builder.setSpan(
                CustomQuoteSpan(quoteColor, stripeWidth, quoteGapWidth),
                start,
                end,
                flag
            )
        }
        if (bulletColor != COLOR_DEFAULT) {
            builder.setSpan(
                CustomBulletSpan(bulletColor, bulletRadius, bulletGapWidth),
                start,
                end,
                flag
            )
        }
        if (fontSize != -1) {
            builder.setSpan(AbsoluteSizeSpan(fontSize, fontSizeIsDp), start, end, flag)
        }
        if (proportion != -1f) {
            builder.setSpan(RelativeSizeSpan(proportion), start, end, flag)
        }
        if (xProportion != -1f) {
            builder.setSpan(ScaleXSpan(xProportion), start, end, flag)
        }
        if (lineHeight != -1) {
            builder.setSpan(CustomLineHeightSpan(lineHeight, alignLine), start, end, flag)
        }
        if (isStrikethrough) {
            builder.setSpan(StrikethroughSpan(), start, end, flag)
        }
        if (isUnderline) {
            builder.setSpan(UnderlineSpan(), start, end, flag)
        }
        if (isSuperscript) {
            builder.setSpan(SuperscriptSpan(), start, end, flag)
        }
        if (isSubscript) {
            builder.setSpan(SubscriptSpan(), start, end, flag)
        }
        if (isBold) {
            builder.setSpan(StyleSpan(Typeface.BOLD), start, end, flag)
        }
        if (isItalic) {
            builder.setSpan(StyleSpan(Typeface.ITALIC), start, end, flag)
        }
        if (isBoldItalic) {
            builder.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, end, flag)
        }
        fontFamily?.let { builder.setSpan(TypefaceSpan(it), start, end, flag) }
        typeface?.let { builder.setSpan(CustomTypefaceSpan(it), start, end, flag) }
        alignment?.let { builder.setSpan(AlignmentSpan.Standard(it), start, end, flag) }
        clickSpan?.let { builder.setSpan(it, start, end, flag) }
        url?.let { builder.setSpan(URLSpan(it), start, end, flag) }
        if (blurRadius != -1f) {
            builder.setSpan(
                MaskFilterSpan(BlurMaskFilter(blurRadius, style)),
                start,
                end,
                flag
            )
        }
        shader?.let { builder.setSpan(ShaderSpan(it), start, end, flag) }
        if (shadowRadius != -1f) {
            builder.setSpan(
                ShadowSpan(shadowRadius, shadowDx, shadowDy, shadowColor),
                start,
                end,
                flag
            )
        }
        spans?.let {
            it.forEach { sp -> builder.setSpan(sp, start, end, flag) }
        }
    }

    private fun updateImage() {
        val start = builder.length
        builder.append("<img>")
        val end = start + 5
        when {
            imageBitmap != null -> builder.setSpan(
                CustomImageSpan(imageBitmap!!, alignImage),
                start,
                end,
                flag
            )
            imageDrawable != null -> builder.setSpan(
                CustomImageSpan(imageDrawable!!, alignImage),
                start,
                end,
                flag
            )
            imageUri != null -> builder.setSpan(
                CustomImageSpan(imageUri!!, alignImage),
                start,
                end,
                flag
            )
            imageResourceId != -1 -> builder.setSpan(
                CustomImageSpan(imageResourceId, alignImage),
                start,
                end,
                flag
            )
        }
    }

    private fun updateSpace() {
        val start = builder.length
        builder.append("< >")
        val end = start + 3
        builder.setSpan(SpaceSpan(spaceSize, spaceColor), start, end, flag)
    }

    internal fun create(): SpannableStringBuilder {
        applyLast()
        return builder
    }

    class CustomLineHeightSpan(private val height: Int, private val verticalAlignment: Int) :
        CharacterStyle(), LineHeightSpan {

        override fun chooseHeight(
            text: CharSequence?,
            start: Int,
            end: Int,
            spanstartv: Int,
            lineHeight: Int,
            fm: Paint.FontMetricsInt?
        ) {
            fm?.let {
                var need = height - (lineHeight + fm.descent - fm.ascent - spanstartv)
                when (verticalAlignment) {
                    ALIGN_TOP -> fm.descent += need
                    ALIGN_CENTER -> {
                        fm.descent += need / 2
                        fm.ascent -= need / 2
                    }
                    else -> fm.ascent -= need
                }
                need = height - (lineHeight + fm.bottom - fm.top - spanstartv)
                when (verticalAlignment) {
                    ALIGN_TOP -> fm.top += need
                    ALIGN_CENTER -> {
                        fm.bottom += need / 2
                        fm.top -= need / 2
                    }
                    else -> fm.top -= need
                }
            }
        }

        override fun updateDrawState(tp: TextPaint?) {
        }
    }

    class SpaceSpan(private val width: Int, private val color: Int = Color.TRANSPARENT) :
        ReplacementSpan() {

        override fun getSize(
            paint: Paint,
            text: CharSequence?,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?
        ): Int = width

        override fun draw(
            canvas: Canvas,
            text: CharSequence?,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            val style = paint.style
            val color = paint.color

            paint.style = Paint.Style.FILL
            paint.color = this.color

            canvas.drawRect(x, top.toFloat(), x + width, bottom.toFloat(), paint)

            paint.style = style
            paint.color = color
        }
    }

    internal class CustomQuoteSpan constructor(
        private val color: Int,
        private val stripeWidth: Int,
        private val gapWidth: Int
    ) : LeadingMarginSpan {

        override fun getLeadingMargin(first: Boolean): Int {
            return stripeWidth + gapWidth
        }

        override fun drawLeadingMargin(
            c: Canvas,
            p: Paint,
            x: Int,
            dir: Int,
            top: Int,
            baseline: Int,
            bottom: Int,
            text: CharSequence,
            start: Int,
            end: Int,
            first: Boolean,
            layout: Layout
        ) {
            val style = p.style
            val color = p.color

            p.style = Paint.Style.FILL
            p.color = this.color

            c.drawRect(
                x.toFloat(),
                top.toFloat(),
                (x + dir * stripeWidth).toFloat(),
                bottom.toFloat(),
                p
            )

            p.style = style
            p.color = color
        }
    }

    internal class CustomBulletSpan constructor(
        private val color: Int,
        private val radius: Int,
        private val gapWidth: Int
    ) : LeadingMarginSpan {
        private var sBulletPath: Path = Path()

        override fun getLeadingMargin(first: Boolean): Int {
            return 2 * radius + gapWidth
        }

        override fun drawLeadingMargin(
            c: Canvas,
            p: Paint,
            x: Int,
            dir: Int,
            top: Int,
            baseline: Int,
            bottom: Int,
            text: CharSequence,
            start: Int,
            end: Int,
            first: Boolean,
            l: Layout
        ) {
            if ((text as Spanned).getSpanStart(this) == start) {
                val style = p.style
                val oldColor = p.color
                p.color = color
                p.style = Paint.Style.FILL
                if (c.isHardwareAccelerated) {
                    if (sBulletPath.isEmpty) {
                        sBulletPath.addCircle(0.0f, 0.0f, radius.toFloat(), Path.Direction.CW)
                    }
                    c.save()
                    c.translate((x + dir * radius).toFloat(), (top + bottom) / 2.0f)
                    c.drawPath(sBulletPath, p)
                    c.restore()
                } else {
                    c.drawCircle(
                        (x + dir * radius).toFloat(),
                        (top + bottom) / 2.0f,
                        radius.toFloat(),
                        p
                    )
                }
                p.color = oldColor
                p.style = style
            }
        }
    }

    internal class CustomTypefaceSpan constructor(private val newType: Typeface) :
        TypefaceSpan("") {

        override fun updateDrawState(textPaint: TextPaint) {
            apply(textPaint, newType)
        }

        override fun updateMeasureState(paint: TextPaint) {
            apply(paint, newType)
        }

        private fun apply(paint: Paint, tf: Typeface) {
            val old = paint.typeface
            val oldStyle = old?.style ?: 0

            val fake = oldStyle and tf.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.isFakeBoldText = true
            }

            if (fake and Typeface.ITALIC != 0) {
                paint.textSkewX = -0.25f
            }

            paint.shader
            paint.typeface = tf
        }
    }

    internal inner class CustomImageSpan : CustomDynamicDrawableSpan {
        private var contentUri: Uri? = null
        private var resourceId: Int = 0

        override var drawable: Drawable? = null
            get() = field ?: if (contentUri != null) {
                try {
                    ctx.contentResolver.openInputStream(contentUri!!).use {
                        field = BitmapDrawable(ctx.resources, BitmapFactory.decodeStream(it))
                        field?.run {
                            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                        }
                    }
                } catch (e: Exception) {
                    Logger.e("Failed to loaded content $contentUri", e)
                }
                field
            } else {
                try {
                    field = ContextCompat.getDrawable(ctx, resourceId)
                    field?.run {
                        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                    }
                } catch (e: Exception) {
                    Logger.e("Unable to find resource: $resourceId", e)
                }
                field
            }

        constructor(b: Bitmap, verticalAlignment: Int) : super(verticalAlignment) {
            drawable = BitmapDrawable(ctx.resources, b)
            drawable?.run {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }
        }

        constructor(d: Drawable, verticalAlignment: Int) : super(verticalAlignment) {
            drawable = d
            drawable?.run {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }
        }

        constructor(uri: Uri, verticalAlignment: Int) : super(verticalAlignment) {
            this.contentUri = uri
        }

        constructor(
            @DrawableRes resourceId: Int,
            verticalAlignment: Int
        ) : super(verticalAlignment) {
            this.resourceId = resourceId
        }
    }

    internal abstract class CustomDynamicDrawableSpan(var verticalAlignment: Int = ALIGN_BOTTOM) :
        ReplacementSpan() {

        abstract var drawable: Drawable?

        private val cachedDrawable: Drawable?
            get() {
                val wr = drawableRef
                var d: Drawable? = wr?.get()
                if (d == null) {
                    d = drawable
                    drawableRef = WeakReference(d)
                }
                return d
            }

        private var drawableRef: WeakReference<Drawable?>? = null

        override fun getSize(
            paint: Paint,
            text: CharSequence?,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?
        ): Int {
            val d = cachedDrawable
            val rect = d?.bounds ?: return 0
            fm?.let {
                val lineHeight = it.bottom - it.top
                if (lineHeight < rect.height()) {
                    when (verticalAlignment) {
                        ALIGN_TOP -> {
                            it.top = it.top
                            it.bottom = rect.height() + it.top
                        }
                        ALIGN_CENTER -> {
                            it.top = -rect.height() / 2 - lineHeight / 4
                            it.bottom = rect.height() / 2 - lineHeight / 4
                        }
                        else -> {
                            it.top = -rect.height() + it.bottom
                            it.bottom = it.bottom
                        }
                    }
                    it.ascent = it.top
                    it.descent = it.bottom
                }
            }
            return rect.right
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence?,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            val d = cachedDrawable
            val rect = d?.bounds ?: return
            canvas.save()
            val lineHeight = bottom - top
            if (rect.height() < lineHeight) {
                val transY = when (verticalAlignment) {
                    ALIGN_TOP -> top.toFloat()
                    ALIGN_CENTER -> (bottom + top - rect.height()) / 2f
                    ALIGN_BASELINE -> y - rect.height().toFloat()
                    else -> bottom - rect.height().toFloat()
                }
                canvas.translate(x, transY)
            } else {
                canvas.translate(x, top.toFloat())
            }
            d.draw(canvas)
            canvas.restore()
        }
    }

    internal class ShaderSpan constructor(private val shader: Shader) : CharacterStyle(),
        UpdateAppearance {

        override fun updateDrawState(tp: TextPaint) {
            tp.shader = shader
        }
    }

    internal class ShadowSpan constructor(
        private val radius: Float,
        private val dx: Float,
        private val dy: Float,
        private val shadowColor: Int
    ) : CharacterStyle(), UpdateAppearance {

        override fun updateDrawState(tp: TextPaint) {
            tp.setShadowLayer(radius, dx, dy, shadowColor)
        }
    }

    class Creator internal constructor(val span: Span) {
        fun create(): SpannableStringBuilder = span.create()
    }

    companion object {
        const val ALIGN_BOTTOM = 0x00
        const val ALIGN_BASELINE = 0x01
        const val ALIGN_CENTER = 0x02
        const val ALIGN_TOP = 0x03

        private val LINE_SEPARATOR = System.getProperty("line.separator")

        @IntDef(ALIGN_BOTTOM, ALIGN_BASELINE, ALIGN_CENTER, ALIGN_TOP)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Align

        private const val COLOR_DEFAULT = -0x1000001
    }
}