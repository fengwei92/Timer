package com.fengw.timer_lib

import java.lang.Exception

object TimerUtil {
    private const val SECOND = 1000
    private const val MINUTE = SECOND * 60
    private const val HOUR = MINUTE * 60
    private const val DAY = HOUR * 24
    const val SPACER = ":"

    /**
     * 获取时：分：秒 -> 19:21:32
     * @param time 差时 = 预计时间 - 现在时间
     */
    fun getTime(time: Long): String {
        var remainingTime = time
        val stringBuffer = StringBuffer()
        if (time < 0) {
            throw Exception("time can not be less than 0 time:${time}ms")
        }
        //时
        val hour = remainingTime / HOUR
        if (hour > 0) {
            remainingTime -= HOUR * hour
        }
        val hourStr = hour.toString()
        if (hourStr.length == 1) {
            stringBuffer.append("0")
        }
        stringBuffer.append(hourStr)
        stringBuffer.append(SPACER)
        //分
        val minute = remainingTime / MINUTE
        if (minute > 0) {
            remainingTime -= MINUTE * minute
        }
        val minuteStr = minute.toString()
        if (minuteStr.length == 1) {
            stringBuffer.append("0")
        }
        stringBuffer.append(minuteStr)
        stringBuffer.append(SPACER)
        //秒
        val second = remainingTime / SECOND
        val secondStr = second.toString()
        if (secondStr.length == 1) {
            stringBuffer.append("0")
        }
        stringBuffer.append(secondStr)
        return stringBuffer.toString()
    }

}