package com.yimulin.mobile.utils

/**
 * @ClassName: NumberUtils
 * @Description: 数字工具类
 * @Author: 常利兵
 * @Date: 2023/5/15 21:15
 **/
object NumberUtils {
    /**
     *
     * 中文数字转阿拉伯数字
     *
     * (长度不能超过long最大值)
     *
     *
     *
     * @param chNum 中文数字
     *
     * @return 阿拉伯数字
     *
     * @author Jarry Leo
     */
    fun ch2Num(chNum: String?): Long {
        var chNum = chNum
        val numLen = intArrayOf(16, 8, 4, 3, 2, 1) //对应下面单位后面多少个零
        val dw = arrayOf("兆", "亿", "万", "千", "百", "十") //中文单位
        val dw1 = arrayOf("兆", "亿", "萬", "仟", "佰", "拾") //中文单位另一版
        val sz = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十") //中文数字
        val sz1 = arrayOf("〇", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾") //中文数字另一版
        if (chNum == null) return 0 //空对象返回0
        for (i in sz.indices) { //统一文字版本
            if (i < dw.size) chNum = chNum!!.replace(dw1[i].toRegex(), dw[i])
            chNum = chNum!!.replace(sz1[i].toRegex(), sz[i])
        }
        chNum = chNum!!.replace("(百.)\\b".toRegex(), "$1十") //正则替换为了匹配中文类似二百五这样的词
        if (chNum.length == 1) {
            for (i in sz.indices) {
                if (chNum == sz[i]) return i.toLong()
            }
            return 0 //中文数字没有这个字
        }
        chNum = strReverse(chNum) //调转输入的字符串
        for (i in dw.indices) {
            if (chNum.contains(dw[i])) {
                val part = chNum.split(dw[i].toRegex(), limit = 2).toTypedArray() //把字符串分割2部分
                val num1 = ch2Num(strReverse(part[1]))
                val num2 = ch2Num(strReverse(part[0]))
                return ((if (num1 == 0L) 1 else num1) * Math.pow(
                    10.0,
                    numLen[i].toDouble()
                ) + num2).toLong()
            }
        }
        val c = chNum.toCharArray()
        var sum: Long = 0
        for (i in c.indices) { //一个个解析数字
            val tem = c[i].toString() //根据索引转成对应数字
            sum += (ch2Num(tem) * Math.pow(10.0, i.toDouble())).toLong() //根据位置给定数字
        }
        return sum
    }

    private fun strReverse(str: String): String { //字符串掉转
        return StringBuilder(str).reverse().toString()
    }

    /**
     *
     * @param num 人民币阿拉伯数字
     *
     * @return 中文大写人民币
     *
     * @author Jarry Leo
     */
    fun upperRMB(num: String): String? {
        val dw = charArrayOf(
            '圆', '拾', '佰', '仟', '萬', '拾', '佰', '仟', '亿', '拾', '佰',
            '仟', '萬', '拾', '佰', '仟', '兆', '拾', '佰', '仟', '萬', '拾', '佰',
            '仟', '亿', '拾', '佰', '仟', '萬', '拾', '佰', '仟'
        ) //单位
        val sz = charArrayOf('零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖') //中文数字
        val s = num.toCharArray() //数字转成字符数组
        val sb = StringBuilder() //创建字符串生成器
        var index = 0
        for (i in s.indices.reversed()) {
            sb.append("" + dw[index++] + sz[s[i].code - 48]) //倒着插入中文数字和单位
        }
        var str = sb.reverse().toString() //字符串反转
        var lastStr: String
        do { //语法调整
            lastStr = str
            str = str.replace("零[零拾佰仟]".toRegex(), "零")
            str = str.replace("零([萬亿兆])".toRegex(), "$1零")
            str = str.replace("亿萬".toRegex(), "亿")
            str = str.replace("兆[萬万]".toRegex(), "兆")
            str = str.replace("零圆".toRegex(), "圆")
        } while (lastStr != str)
        return str
    }
}