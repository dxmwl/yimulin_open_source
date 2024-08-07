package com.yimulin.mobile.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yimulin.mobile.db.entity.ToolsClassEntity
import com.yimulin.mobile.db.entity.ToolsEntity

/**
 * @ClassName: ToolsClassDao
 * @Description: 工具分类
 * @Author: 常利兵
 * @Date: 2023/5/12 16:44
 **/
@Dao
interface ToolsDao {
    @Query("SELECT * FROM tools")
    fun getAll(): List<ToolsEntity>

    @Insert
    fun insertAll(vararg toolsEntity: ToolsEntity)

    @Delete
    fun delete(toolsEntity: ToolsEntity)
}