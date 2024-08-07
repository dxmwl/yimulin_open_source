package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: ReportCompanyApi
 * @Description: 举报
 * @Author: 常利兵
 * @Date: 2023/7/31 11:05
 **/
class ReportCompanyApi:IRequestApi {
    override fun getApi(): String {
        return "api/companyBlackList/reportCompany"
    }

    var companyNumber:String? = ""
    var companyName:String? = ""
    var legalRepresentative:String? = ""
    var reportType:String? = ""
    var reportReason:String? = ""
    var reportImgs:String? = ""
}