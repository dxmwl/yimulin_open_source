package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 密码登录
 */
class PwdLoginApi : IRequestApi {
    override fun getApi(): String {
        return "api/user/loginByPwd"
    }

    /** 手机号 */
    var phone: String? = null

    /** 登录密码 */
    var loginPwd: String? = null
}