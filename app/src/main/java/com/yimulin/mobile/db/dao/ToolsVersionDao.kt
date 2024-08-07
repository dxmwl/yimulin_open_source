package com.yimulin.mobile.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yimulin.mobile.db.entity.ToolsEntity
import com.yimulin.mobile.db.entity.ToolsVersionEntity

/**
 * @ClassName: ToolsVersionDao
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/5/12 17:55
 **/
@Dao
interface ToolsVersionDao {
    @Query("SELECT * FROM toolsVersion")
    fun getAll(): List<ToolsVersionEntity>

    @Insert
    fun insertAll(vararg toolsVersionEntity: ToolsVersionEntity)

    @Delete
    fun delete(toolsVersionEntity: ToolsVersionEntity)
}