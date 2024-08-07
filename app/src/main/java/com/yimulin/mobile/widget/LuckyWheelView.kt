package com.yimulin.mobile.widget

/**
 * @ClassName: LuckyWheelView
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/8/10 9:46
 **/
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class LuckyWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF: RectF = RectF()
    private val colors: IntArray = intArrayOf(
        Color.parseColor("#FFC107"),
        Color.parseColor("#F44336"),
        Color.parseColor("#9C27B0"),
        Color.parseColor("#2196F3"),
        Color.parseColor("#4CAF50"),
        Color.parseColor("#FF5722")
    )
    private val icons: MutableList<Bitmap> = mutableListOf()
    private val texts: MutableList<String> = mutableListOf()

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var sweepAngle = 0f
    private var startAngle = 0f

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = Math.min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        centerX = width / 2f
        centerY = height / 2f
        radius = Math.min(centerX, centerY)

        drawSectors(canvas)
//        drawIcons(canvas)
        drawTexts(canvas)
    }

    private fun drawSectors(canvas: Canvas) {
        sweepAngle = 360f / colors.size
        startAngle = 0f

        for (i in colors.indices) {
            paint.color = colors[i]
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)
            startAngle += sweepAngle
        }
    }

    private fun drawIcons(canvas: Canvas) {
        val iconSize = radius / 4f

        for (i in icons.indices) {
            val angle = startAngle + (sweepAngle * i) + (sweepAngle / 2f)
            val x = centerX + (radius / 2f) * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + (radius / 2f) * Math.sin(Math.toRadians(angle.toDouble())).toFloat()

            val icon = Bitmap.createScaledBitmap(icons[i], iconSize.toInt(), iconSize.toInt(), false)
            canvas.drawBitmap(icon, x - (iconSize / 2f), y - (iconSize / 2f), null)
        }
    }

    private fun drawTexts(canvas: Canvas) {
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = Color.WHITE
        textPaint.textSize = radius / 12f

        for (i in texts.indices) {
            val angle = startAngle + (sweepAngle * i) + (sweepAngle / 2f)
            val x = centerX + (radius / 2f) * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + (radius / 2f) * Math.sin(Math.toRadians(angle.toDouble())).toFloat()

            val textWidth = textPaint.measureText(texts[i])
            canvas.drawText(texts[i], x - (textWidth / 2f), y + (radius / 4f), textPaint)
        }
    }

    fun setIcons(icons: List<Bitmap>) {
        this.icons.clear()
        this.icons.addAll(icons)
        invalidate()
    }

    fun setTexts(texts: List<String>) {
        this.texts.clear()
        this.texts.addAll(texts)
        invalidate()
    }
}