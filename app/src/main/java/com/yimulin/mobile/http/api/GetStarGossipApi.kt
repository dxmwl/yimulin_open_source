package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestServer

/**
 * @ClassName: GetStarGossipApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/1/17 0017 15:14
 **/
class GetStarGossipApi : IRequestApi, IRequestServer {
    override fun getApi(): String {
        return "mingxingbagua/apis.php?type=json"
    }

    override fun getHost(): String {
        return "https://dayu.qqsuu.cn/"
    }
}