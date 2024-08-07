package com.yimulin.mobile.utils

import java.util.*

/**
 * @ClassName: MorseCodeUtils
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/6/21 16:22
 **/
object MorseCodeUtils {
    private val morseCodeMap: Map<Char, String> = mapOf(
        'A' to ".-",
        'B' to "-...",
        'C' to "-.-.",
        'D' to "-..",
        'E' to ".",
        'F' to "..-.",
        'G' to "--.",
        'H' to "....",
        'I' to "..",
        'J' to ".---",
        'K' to "-.-",
        'L' to ".-..",
        'M' to "--",
        'N' to "-.",
        'O' to "---",
        'P' to ".--.",
        'Q' to "--.-",
        'R' to ".-.",
        'S' to "...",
        'T' to "-",
        'U' to "..-",
        'V' to "...-",
        'W' to ".--",
        'X' to "-..-",
        'Y' to "-.--",
        'Z' to "--..",
        '0' to "-----",
        '1' to ".----",
        '2' to "..---",
        '3' to "...--",
        '4' to "....-",
        '5' to ".....",
        '6' to "-....",
        '7' to "--...",
        '8' to "---..",
        '9' to "----.",
        '.' to ".-.-.-",
        ',' to "--..--",
        '?' to "..--..",
        '\'' to ".----.",
        '!' to "-.-.--",
        '/' to "-..-.",
        '(' to "-.--.",
        ')' to "-.--.-",
        '&' to ".-...",
        ':' to "---...",
        ';' to "-.-.-.",
        '=' to "-...-",
        '+' to ".-.-.",
        '-' to "-....-",
        '_' to "..--.-",
        '"' to ".-..-.",
        '$' to "...-..-",
        '@' to ".--.-."
    )

    fun encodeToMorseCode(text: String): String {
        val sb = StringBuilder()
        for (char in text.toUpperCase()) {
            if (char == ' ') {
                sb.append(" ")
            } else {
                val morseCode = morseCodeMap[char]
                if (morseCode != null) {
                    sb.append(morseCode).append(" ")
                }
            }
        }
        return sb.toString().trim()
    }

    fun decodeFromMorseCode(morseCode: String): String {
        val sb = StringBuilder()
        val words = morseCode.split("   ")
        for (word in words) {
            val chars = word.split(" ")
            for (char in chars) {
                val key = morseCodeMap.filterValues { it == char }.keys.firstOrNull()
                if (key != null) {
                    sb.append(key)
                }
            }
            sb.append(" ")
        }
        return sb.toString().trim()
    }
}