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
@Entity(tableName = "toolsClass")
data class ToolsClassEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "classId") val classId: Int?,
    @ColumnInfo(name = "className") val className: String?,
    @ColumnInfo(name = "isLocal") val isLocal: Boolean? = false,
    @ColumnInfo(name = "sort") val sort: Int? = 0,
)