package com.yimulin.mobile.http.model

/**
 * @ClassName: ToolsDto
 * @Description: 工具类实体类
 * @Author: 常利兵
 * @Date: 2023/5/12 14:43
 **/
data class ToolsBeanDto(
    val version: Int,
    val data: ArrayList<ToolClassDto>
)

data class ToolClassDto(
    val isLocal: Boolean,
    val className: String,
    val classId: Int,
    val children: ArrayList<ToolsDto>
)

data class ToolsDto(
    val toolsId: Int,
    val toolsName: String,
    val url: String,
    val isFullScreen: Boolean,
    val isNeedSearch: Boolean,
    //是否需要观看广告
    val isNeedViewAd: Boolean,
    //服务类型（0：免费 1：会员免费 2：按次收费）
    val serviceType: Int = 0,
)