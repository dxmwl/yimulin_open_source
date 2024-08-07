package com.yimulin.mobile.db

import androidx.room.Room
import com.yimulin.mobile.app.AppApplication

/**
 * @ClassName: DbHelper
 * @Description: 数据库辅助类
 * @Author: 常利兵
 * @Date: 2023/5/12 17:14
 **/
object DbHelper {

    val db = Room.databaseBuilder(
        AppApplication.getApplication(),
        AppDatabase::class.java, "database-yimulin"
    )
        .fallbackToDestructiveMigration()
        .build()
}