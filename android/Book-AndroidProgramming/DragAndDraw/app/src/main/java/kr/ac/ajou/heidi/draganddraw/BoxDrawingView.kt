package kr.ac.ajou.heidi.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

// 주 생성자는 XML로부터 뷰를 인플레이트할 때 사용한다.

class BoxDrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    constructor(context: Context) : this(context, null)
    // 코드에서 뷰를 생성할 때 사용한다.

    companion object {
        val TAG = BoxDrawingView::class.java.simpleName
    }

    private var currentBox: Box? = null
    private var boxen = arrayListOf<Box>()
    private val boxPaint = Paint()
    private val backgroundPaint = Paint()
    private var stuff: Int = 0

    init {
        boxPaint.color = Color.parseColor("#222222")
        backgroundPaint.color = Color.parseColor("#555555")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val current = PointF(event?.x ?: 0f, event?.y ?: 0f)
        var action = ""
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                currentBox = Box(current)
                currentBox?.let {
                    boxen.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                currentBox?.let {
                    it.current = current
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                currentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
            }
        }

        Log.i(TAG, "$action at x= ${current.x} y= ${current.y}")
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        canvas.drawPaint(backgroundPaint)

        for (i in 0.until(boxen.size)) {
            val box = boxen[i]
            val left = Math.min(box.origin.x, box.current.x)
            val right = Math.max(box.origin.x, box.current.x)
            val top = Math.min(box.origin.y, box.current.y)
            val bottom = Math.max(box.origin.y, box.current.y)

            canvas.drawRect(left, top, right, bottom, boxPaint)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt("stuff", this.stuff)
        bundle.putSerializable("boxen", boxen)
        return bundle


    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val bundle = state
            this.stuff = bundle.getInt("stuff")
            val savedState = bundle.getParcelable("superState") as Parcelable
            boxen = bundle.getSerializable("boxen") as ArrayList<Box>
            super.onRestoreInstanceState(savedState)
        }
        super.onRestoreInstanceState(state)
    }
}

