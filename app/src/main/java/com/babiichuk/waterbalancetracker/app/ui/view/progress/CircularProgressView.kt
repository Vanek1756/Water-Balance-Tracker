package com.babiichuk.waterbalancetracker.app.ui.view.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.extensions.getColor
import com.babiichuk.waterbalancetracker.app.ui.extensions.toPx

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val typeface = ResourcesCompat.getFont(context, R.font.jet_brains_mono_regular)

    private var progress = 50
    private var maxProgress = 100
    private var waterRate = 2400
    private var currentRate = 0
    private val rect = RectF()

    private val arrayOfGradient = intArrayOf(
        getColor(R.color.colorBlueVariant6),
        getColor(R.color.colorBlueVariant7),
        getColor(R.color.colorBlueVariant8),
    )

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = getColor(R.color.colorWhite)
        strokeWidth = toPx(20f)
        style = Paint.Style.FILL_AND_STROKE
    }
    private val topProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = toPx(20f)
    }
    private val bottomProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = toPx(20f)
        alpha = 50
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = getColor(R.color.colorBlack)
        textSize = toPx(32f)
        textAlign = Paint.Align.CENTER
        typeface = this@CircularProgressView.typeface
    }
    private val textSubPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = getColor(R.color.colorBlack)
        textSize = toPx(12f)
        textAlign = Paint.Align.CENTER
        typeface = this@CircularProgressView.typeface
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Окреслення кола
        val width = width.toFloat()
        val height = height.toFloat()
        val radius = Math.min(width, height) / 2
        val centerX = width / 2
        val centerY = height / 2
        val startAngle = -90f

        rect.set(
            centerX - (radius - toPx(10)) + 10,
            centerY - (radius - toPx(10)) + 10,
            centerX + (radius - toPx(10)) - 10,
            centerY + (radius - toPx(10)) - 10
        )
        canvas.drawArc(rect, startAngle, 360f, false, backgroundPaint)

        rect.set(
            centerX - (radius - toPx(20)) + 10,
            centerY - (radius - toPx(20)) + 10,
            centerX + (radius - toPx(20)) - 10,
            centerY + (radius - toPx(20)) - 10
        )
        bottomProgressPaint.shader = LinearGradient(
            rect.left, rect.top, rect.right, rect.bottom,
            arrayOfGradient,
            null,
            Shader.TileMode.CLAMP
        )
        canvas.drawArc(rect, startAngle, 360f, false, bottomProgressPaint)

        val sweepAngle = 360f * progress / maxProgress
        topProgressPaint.shader = LinearGradient(
            rect.left, rect.top, rect.right, rect.bottom,
            arrayOfGradient,
            null,
            Shader.TileMode.CLAMP
        )
        canvas.drawArc(rect, startAngle, sweepAngle, false, topProgressPaint)

        canvas.drawText("$progress%", centerX, centerY, textPaint)
        canvas.drawText("of $waterRate ml", centerX, centerY + 50, textSubPaint)
    }

    fun setCurrentRate(currentRate: Int) {
        this.currentRate = currentRate
        calculatePercentage()
        invalidate()
    }

    fun setWaterRate(waterRate: Int) {
        this.waterRate = waterRate
    }

    private fun calculatePercentage() {
        if (waterRate == 0) {
            throw IllegalArgumentException("maxRate cannot be 0")
        }
        this.progress = ((currentRate.toDouble() / waterRate.toDouble()) * 100).toInt()
    }
}
