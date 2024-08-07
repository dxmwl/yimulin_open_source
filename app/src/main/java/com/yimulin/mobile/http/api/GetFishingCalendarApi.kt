package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestServer

/**
 * @ClassName: GetFishingCalendarApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/1/17 0017 14:55
 **/
class GetFishingCalendarApi:IRequestApi, IRequestServer {
    override fun getApi(): String {
        return "moyurili/apis.php?type=json"
    }

    override fun getHost(): String {
        return "https://dayu.qqsuu.cn/"
    }
}