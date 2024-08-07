package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestServer

/**
 * @ClassName: GetTouchingTheFishDailyApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/1/17 0017 15:11
 **/
class GetTouchingTheFishDailyApi : IRequestApi, IRequestServer {
    override fun getApi(): String {
        return "moyuribao/apis.php?type=json"
    }

    override fun getHost(): String {
        return "https://dayu.qqsuu.cn/"
    }
}