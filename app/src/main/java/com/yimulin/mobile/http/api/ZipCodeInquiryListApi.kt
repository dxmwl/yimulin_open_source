package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: ZipCodeInquiryListApi
 * @Description: 邮编查询
 * @Author: 常利兵
 * @Date: 2023/6/1 15:13
 **/
class ZipCodeInquiryListApi:IRequestApi {
    override fun getApi(): String {
        return "api/search/zipCodeInquiry"
    }

    var keyWord:String? = ""
    var pageNum = 1
    var pageSize = 20

    data class ZipCodeDto(
        val id:String,
        val addressName:String,
        val zipCode:String,
    )
}