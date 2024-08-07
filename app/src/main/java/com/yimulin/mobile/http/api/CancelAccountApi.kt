package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * Created with Android studio
 * Description:注销账号
 * @Author: 常利兵
 * DateTime: 2023-02-11 15:35
 */
class CancelAccountApi:IRequestApi {
    override fun getApi(): String {
        return "api/user/cancelAccount"
    }
}