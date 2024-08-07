package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 获取验证码
 */
class GetCodeApi : IRequestApi {

    override fun getApi(): String {
        return "api/user/sendCode"
    }

    /** 手机号 */
    private var phone: String? = null

    //验证码类型(1:登录注册验证 2:重置密码,3:验证身份))
    var type: Int? = null

    fun setPhone(phone: String?): GetCodeApi = apply {
        this.phone = phone
    }
}