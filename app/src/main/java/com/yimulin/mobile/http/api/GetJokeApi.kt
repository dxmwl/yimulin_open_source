package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestServer

/**
 * @ClassName: GetJokeApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/1/17 0017 15:16
 **/
class GetJokeApi : IRequestApi, IRequestServer {
    override fun getApi(): String {
        return "neihanduanzi/apis.php?type=json"
    }

    override fun getHost(): String {
        return "https://dayu.qqsuu.cn/"
    }
}