package com.fengw.timer_lib

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import android.content.res.TypedArray
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity


class TimerView(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var countDownTimer: CountDownTimer? = null
    private val mTvHour = TextView(context)//时
    private val mTvMinute = TextView(context)//分
    private val mTvSecond = TextView(context)//秒
    private val mTvSpacer1 = TextView(context)//中间间隔符 ：或 - 随意
    private val mTvSpacer2 = TextView(context)
    val DEFAULT_TEXT_COLOR = R.color.color_ef7075
    val DEFAULT_SPACER_TEXT_COLOR = R.color.white
    val DEFAULT_TEXT_BACKGROUND_DRAWABLE = R.drawable.timer_text_bg
    val DEFAULT_TEXT_SIZE = 14F
    val DEFAULT_SPACER_TEXT_SIZE = 12F
    val DEFAULT_SPACER_TEXT = ":"
    val DEFAULT_TEXT_WIDTH = dp2px(20)
    val DEFAULT_TEXT_HEIGHT = dp2px(20)
    val DEFAULT_SPACER_TEXT_WIDTH = dp2px(10)
    val DEFAULT_SPACER_TEXT_HEIGHT = dp2px(20)
    private var onTimerCallback: OnTimerCallback? = null

    init {
        val array: TypedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.timer_style)
        val textColor = array.getColor(
            R.styleable.timer_style_textColor,
            resources.getColor(DEFAULT_TEXT_COLOR)
        )
        val textSize = array.getDimension(R.styleable.timer_style_textSize, DEFAULT_TEXT_SIZE)
        val textWidth = array.getInteger(R.styleable.timer_style_textViewWidth, DEFAULT_TEXT_WIDTH)
        val textHeight =
            array.getInteger(R.styleable.timer_style_textViewHeight, DEFAULT_TEXT_HEIGHT)
        val backgroundDrawableId = array.getResourceId(
            R.styleable.timer_style_textBackgroundDrawable,
            DEFAULT_TEXT_BACKGROUND_DRAWABLE
        )
        val spacerTextSize =
            array.getDimension(R.styleable.timer_style_spacerTextSize, DEFAULT_SPACER_TEXT_SIZE)
        val spacerTextColor =
            array.getColor(
                R.styleable.timer_style_spacerTextColor,
                resources.getColor(DEFAULT_SPACER_TEXT_COLOR)
            )
        var spacerText = array.getString(R.styleable.timer_style_spacerText)
        if (TextUtils.isEmpty(spacerText)) {
            spacerText = DEFAULT_SPACER_TEXT
        }
        mTvHour.setTextColor(textColor)
        mTvHour.textSize = textSize
        mTvHour.width = textWidth
        mTvHour.height = textHeight
        mTvHour.setBackgroundResource(backgroundDrawableId)
        mTvHour.gravity = Gravity.CENTER

        mTvMinute.setTextColor(textColor)
        mTvMinute.textSize = textSize
        mTvMinute.width = textWidth
        mTvMinute.height = textHeight
        mTvMinute.setBackgroundResource(backgroundDrawableId)
        mTvMinute.gravity = Gravity.CENTER

        mTvSecond.setTextColor(textColor)
        mTvSecond.textSize = textSize
        mTvSecond.width = textWidth
        mTvSecond.height = textHeight
        mTvSecond.setBackgroundResource(backgroundDrawableId)
        mTvSecond.gravity = Gravity.CENTER

        mTvSpacer1.textSize = spacerTextSize
        mTvSpacer1.setTextColor(spacerTextColor)
        mTvSpacer1.width = DEFAULT_SPACER_TEXT_WIDTH
        mTvSpacer1.height = DEFAULT_SPACER_TEXT_HEIGHT
        mTvSpacer1.text = spacerText
        mTvSpacer1.gravity = Gravity.CENTER

        mTvSpacer2.textSize = spacerTextSize
        mTvSpacer2.setTextColor(spacerTextColor)
        mTvSpacer2.width = DEFAULT_SPACER_TEXT_WIDTH
        mTvSpacer2.height = DEFAULT_SPACER_TEXT_HEIGHT
        mTvSpacer2.text = spacerText
        mTvSpacer2.gravity = Gravity.CENTER

        addView(mTvHour)
        addView(mTvSpacer1)
        addView(mTvMinute)
        addView(mTvSpacer2)
        addView(mTvSecond)
        orientation = HORIZONTAL
        array.recycle()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val parentHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        val parentWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        var parentNeedWidth = 0
        var parentNeedHeight = 0
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val childViewLP = childView.layoutParams
            val childWidthMeasureSpec =
                getChildMeasureSpec(
                    widthMeasureSpec,
                    paddingLeft + paddingRight,
                    childViewLP.width
                )
            val childHeightMeasureSpec =
                getChildMeasureSpec(
                    heightMeasureSpec,
                    paddingBottom + paddingTop,
                    childViewLP.height
                )
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            val childMeasureWidth = childView.measuredWidth
            val childMeasureHeight = childView.measuredHeight
            parentNeedWidth += childMeasureWidth
            parentNeedHeight =
                if (parentNeedHeight < childMeasureHeight) childMeasureHeight else parentNeedHeight
        }
        val realWidth =
            if (parentWidthMode == MeasureSpec.EXACTLY) parentWidthSize else parentNeedWidth
        val realHeight =
            if (parentHeightMode == MeasureSpec.EXACTLY) parentHeightSize else parentNeedHeight
        setMeasuredDimension(realWidth, realHeight)

    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = paddingLeft
        var top = paddingTop
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val measureWidth = childView.measuredWidth
            val measureHeight = childView.measuredHeight
            var right = left + measureWidth
            var bottom = top + measureHeight
            childView.layout(left, top, right, bottom)
            left = right
        }

    }


    fun start(millis: Long, countDownInterval: Long, onTimerCallback: OnTimerCallback) {
        object : CountDownTimer(millis, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val timeStr = TimerUtil.getTime(millisUntilFinished)
                val split = timeStr.split(TimerUtil.SPACER)
                mTvHour.text = split[0]
                mTvMinute.text = split[1]
                mTvSecond.text = split[2]
                onTimerCallback.onTick(millisUntilFinished)
            }

            override fun onFinish() {
                onTimerCallback.onFinish()
            }

        }.start()
    }


    fun getHourTextView(): TextView {
        return mTvHour
    }

    fun getMinuteTextView(): TextView {
        return mTvMinute
    }

    fun getSecondTextView(): TextView {
        return mTvSecond
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        )
            .toInt()
    }

    fun sp2px(sp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp.toFloat(),
            Resources.getSystem().displayMetrics
        )
            .toInt()
    }
}