package com.yimulin.mobile.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @ClassName: BirthdayUtils
 * @Description: 生日工具类
 * @Author: 常利兵
 * @Date: 2023/5/9 14:47
 **/
object BirthdayUtils {

    /*** 计算星座
     *  @param birthday
     *   @return
     * */
    fun calculateTheConstellations(birthday: Long): String {
        val simpleDateFormat = SimpleDateFormat("Mdd")
        val birthdayDate = Date(birthday)
        val format: String = simpleDateFormat.format(birthdayDate)
        return when (format.toInt()) {
            in 121..219 -> {
                "水瓶座"
            }
            in 220..320 -> {
                "双鱼座"
            }
            in 321..420 -> {
                "白羊座"
            }
            in 421..521 -> {
                "金牛座"
            }
            in 522..621 -> {
                "双子座"
            }
            in 622..722 -> {
                "巨蟹座"
            }
            in 723..823 -> {
                "狮子座"
            }
            in 824..923 -> {
                "处女座"
            }
            in 924..1023 -> {
                "天秤座"
            }
            in 1024..1122 -> {
                "天蝎座"
            }
            in 1123..1221 -> {
                "射手座"
            }
            else -> {
                "魔蝎座"
            }
        }
    }

    /**
     * 计算年龄
     */
    fun calculateAge(birthday: Long): Int {
        val birthDate = Date(birthday)
        // 当前日历
        val nowCalendar = Calendar.getInstance()
        // 生日大于当前日期
        require(!nowCalendar.before(birthDate)) { "The birth date is before current time, it's unbelievable" }
        // 当前年月日
        val yearNow = nowCalendar[Calendar.YEAR]
        val monthNow = nowCalendar[Calendar.MONTH]
        val dayNow = nowCalendar[Calendar.DAY_OF_MONTH]
        // 出生日期年月日
        val birthCalendar = Calendar.getInstance()
        birthCalendar.time = birthDate
        val yearBirth = birthCalendar[Calendar.YEAR]
        val monthBirth = birthCalendar[Calendar.MONTH]
        val dayBirth = birthCalendar[Calendar.DAY_OF_MONTH]
        // 粗计算年龄
        var age = yearNow - yearBirth
        // 当前月份小于出生月份年龄减一
        if (monthNow < monthBirth) {
            age--
        } else if (monthNow == monthBirth && dayNow < dayBirth) {
            age--
        }
        // 返回年龄值
        return age
    }
}