package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 用户登录
 */
class LoginApi : IRequestApi {

    override fun getApi(): String {
        return "api/user/loginByCode"
    }

    /** 手机号 */
    private var phone: String? = null

    /** 登录密码 */
    private var password: String? = null

    /**
     * 验证码
     */
    var code: String? = null

    fun setPhone(phone: String?): LoginApi = apply {
        this.phone = phone
    }

    fun setPassword(password: String?): LoginApi = apply {
        this.password = password
    }

    class TokenResult {

        private val token: String? = null

        fun getToken(): String? {
            return token
        }
    }
}