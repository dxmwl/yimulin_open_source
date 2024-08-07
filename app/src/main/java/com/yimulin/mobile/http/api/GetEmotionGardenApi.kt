package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestServer

/**
 * @ClassName: GetEmotionGardenApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/1/17 0017 15:21
 **/
class GetEmotionGardenApi : IRequestApi, IRequestServer {
    override fun getApi(): String {
        return "qingganhuayuan/apis.php?type=json"
    }

    override fun getHost(): String {
        return "https://dayu.qqsuu.cn/"
    }
}