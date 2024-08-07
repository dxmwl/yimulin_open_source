package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 获取用户信息
 */
class UserInfoApi : IRequestApi {

    override fun getApi(): String {
        return "api/user/userInfo"
    }

    data class UserInfoDto(
        val userId: String,
        val nickName: String,
        val avatar: String,
        val phonenumber: String,
        val memberStatus: Boolean,
        val memberTime: String,
    )
}