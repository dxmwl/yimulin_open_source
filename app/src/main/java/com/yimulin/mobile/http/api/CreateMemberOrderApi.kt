package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: CreateMemberOrderApi
 * @Description: 创建支付订单
 * @Author: 常利兵
 * @Date: 2023/5/30 23:28
 **/
class CreateMemberOrderApi : IRequestApi {

    override fun getApi(): String {
        return "api/pay/createOrderMember"
    }

    var payListId: String = ""

    data class PayOrderInfoDto(
        val orderNumber: String
    )
}