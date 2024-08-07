package com.yimulin.mobile.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @ClassName: ToolsVersionEntity
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/5/12 17:54
 **/
@Entity(tableName = "toolsVersion")
data class ToolsVersionEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "version") val version: Int?,
)