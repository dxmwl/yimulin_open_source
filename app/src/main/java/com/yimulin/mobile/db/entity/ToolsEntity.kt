package com.yimulin.mobile.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @ClassName: ToolsClassEntity
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/5/12 16:38
 **/
@Entity(tableName = "tools")
data class ToolsEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "classId") val classId: Int?,
    @ColumnInfo(name = "toolsId") val toolsId: Int?,
    @ColumnInfo(name = "toolsName") val toolsName: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "isFullScreen") val isFullScreen: Boolean? = false,
    @ColumnInfo(name = "serviceType", defaultValue = "0") val serviceType: Int = 0,
    @ColumnInfo(name = "sort") val sort: Int? = 0,
)