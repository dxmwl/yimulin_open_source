package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: SearchCompanyBlackApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/8/1 23:15
 **/
class SearchCompanyBlackApi:IRequestApi {
    override fun getApi(): String {
        return "api/companyBlackList/searchCompany"
    }

    var keyWord:String? = ""
    var pageNum:Int = 1
    var pageSize:Int = 20

    data class CompanyBlackDto(
        val id:String,
        val companyNumber:String,
        val companyName:String,
        val legalRepresentative:String,
        val reportReason:String,
        val reportType:String,
        val reportImgs:String?,
        val reportVideo:String?,
    )
}