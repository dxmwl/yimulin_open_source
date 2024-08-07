package com.yimulin.mobile.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yimulin.mobile.db.entity.ToolsClassEntity

/**
 * @ClassName: ToolsClassDao
 * @Description: 工具分类
 * @Author: 常利兵
 * @Date: 2023/5/12 16:44
 **/
@Dao
interface ToolsClassDao {

    @Query("select * from toolsClass where isLocal = (:isLocal)")
    fun getAll(isLocal: Boolean): List<ToolsClassEntity>

    @Insert
    fun insertAll(vararg toolsClassEntity: ToolsClassEntity)

    @Delete
    fun delete(toolsClassEntity: ToolsClassEntity)
}