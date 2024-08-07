package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 用户反馈接口
 */
class UserFeedBackApi:IRequestApi {
    override fun getApi(): String {
        return "api/feedBack/commitFeedBack"
    }
    var feedBackClass = ""
    var feedBackContent = ""
    var feedBackImgs = ""
}