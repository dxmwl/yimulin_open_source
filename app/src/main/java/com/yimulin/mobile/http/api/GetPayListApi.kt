package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: GetPayListApi
 * @Description: 获取支付列表
 * @Author: 常利兵
 * @Date: 2023/5/29 22:28
 **/
class GetPayListApi:IRequestApi {
    override fun getApi(): String {
        return "api/pay/getPayList"
    }

    data class PaylistDto(
        val payListId:String,
        val title:String,
        val originalPrice:String,
        val currentPrice:String,
        val label:String,
        val dayNum:String,
        var checked:Boolean = false
    )
}