package com.yimulin.mobile.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yimulin.mobile.db.dao.MakeAChoiceDto
import com.yimulin.mobile.db.dao.ToolsClassDao
import com.yimulin.mobile.db.dao.ToolsDao
import com.yimulin.mobile.db.dao.ToolsVersionDao
import com.yimulin.mobile.db.entity.MakeAChoiceEntity
import com.yimulin.mobile.db.entity.ToolsClassEntity
import com.yimulin.mobile.db.entity.ToolsEntity
import com.yimulin.mobile.db.entity.ToolsVersionEntity

/**
 * @ClassName: AppDatabase
 * @Description: 数据库
 * @Author: 常利兵
 * @Date: 2023/5/12 16:46
 **/
@Database(
    entities = [ToolsClassEntity::class, ToolsEntity::class, ToolsVersionEntity::class, MakeAChoiceEntity::class],
    version = 2,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun toolsClassDao(): ToolsClassDao

    abstract fun toolsDao(): ToolsDao

    abstract fun toolsVersionDao(): ToolsVersionDao

    abstract fun makeAChoiceDao(): MakeAChoiceDto

}