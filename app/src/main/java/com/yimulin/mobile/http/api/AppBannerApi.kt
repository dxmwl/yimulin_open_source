package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: AppBannerApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/1/16 0016 10:02
 **/
class AppBannerApi:IRequestApi {
    override fun getApi(): String {
        return "api/banner/getBanner"
    }

    var showType:Int = 0

    data class BannerBean(
        val bannerImg:String,
        val title:String?,
        val params:String?,
        //0:仅展示 1：下载APP
        val type:Int,
    )
}