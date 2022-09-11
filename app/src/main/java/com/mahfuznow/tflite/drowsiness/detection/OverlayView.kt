package com.mahfuznow.tflite.drowsiness.detection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import org.tensorflow.lite.examples.objectdetection.R
import org.tensorflow.lite.support.label.Category

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var result: Category? = null
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    init {
        initPaints()
    }

    fun clear() {
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = ContextCompat.getColor(context!!, R.color.bounding_box_color)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val text = if (result == null) " " else result!!.label
        canvas.drawText(text, 100F, 100F, textPaint)
    }

    fun setResults(probability: MutableList<Category>) {
        probability.forEach {
            Log.d("TAG", "setResults: ${it.score}")
        }
        val maxValueIndex = probability.withIndex().maxByOrNull { it.value.score }?.index
        Log.d("TAG", "$maxValueIndex")
        Log.d("TAG", "------------------")

        maxValueIndex?.let {
            result = probability[maxValueIndex]
        }
    }
}


