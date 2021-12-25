package com.fengw.timer_lib

interface OnTimerCallback {

    public fun onTick(millisUntilFinished: Long)

    public fun onFinish()

}