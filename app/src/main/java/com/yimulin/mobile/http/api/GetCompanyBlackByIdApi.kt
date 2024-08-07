package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: GetCompanyBlackByIdApi
 * @Description: 查询企业黑名单详情
 * @Author: 常利兵
 * @Date: 2023/8/2 23:45
 **/
class GetCompanyBlackByIdApi:IRequestApi {
    override fun getApi(): String {
        return "api/companyBlackList/getCompanyBlackById"
    }

    var companyBlackId:String? = ""
}