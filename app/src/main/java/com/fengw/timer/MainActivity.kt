package com.fengw.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.fengw.timer_lib.OnTimerCallback
import com.fengw.timer_lib.TimerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val timerView = findViewById<TimerView>(R.id.timer)
        timerView.start(60*1000*60*2, 1000, object : OnTimerCallback{
            override fun onTick(millisUntilFinished: Long) {
                //倒计时过程
            }

            override fun onFinish() {
                //倒计时结束
            }

        })
    }
}