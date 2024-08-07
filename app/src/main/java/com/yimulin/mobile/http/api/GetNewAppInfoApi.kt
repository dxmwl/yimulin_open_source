package com.yimulin.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 获取最新的安装包信息
 */
class GetNewAppInfoApi : IRequestApi {
    override fun getApi(): String {
        return "api/common/getNewAppInfo"
    }

    data class NewAppInfoDto(
        val channel: Int,
        val createTime: String,
        val downloadUrl: String,
        val id: String,
        val updateContent: String,
        val updateTime: String,
        val forceUpdate: Boolean,
        val versionCode: Int,
        val versionName: String
    )
}