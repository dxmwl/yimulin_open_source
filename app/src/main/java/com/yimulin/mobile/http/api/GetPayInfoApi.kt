package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: GetPayInfoApi
 * @Description: 获取预支付信息
 * @Author: 常利兵
 * @Date: 2023/5/30 23:33
 **/
class GetPayInfoApi : IRequestApi {


    override fun getApi(): String {
        return "api/pay/createPayInfoByOrderNumber"
    }

    var orderNumber: String = ""
    var type: Int = 1

    data class PayInfoDto(
        val wechatPayInfo: WechatPayInfo,
        val aliPayOrderInfo: String
    )

    data class WechatPayInfo(
        val appid: String,
        val noncestr: String,
        val packageValue: String,
        val partnerid: String,
        val prepayId: String,
        val sign: String,
        val timestamp: String
    )
}