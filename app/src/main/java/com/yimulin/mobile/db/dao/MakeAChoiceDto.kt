package com.yimulin.mobile.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yimulin.mobile.db.entity.MakeAChoiceEntity
import com.yimulin.mobile.db.entity.ToolsClassEntity

/**
 * @ClassName: MakeAChoiceDto
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/8/11 19:56
 **/
@Dao
interface MakeAChoiceDto {

    @Query("select * from makeAChoice")
    fun selectAllChoice(): List<MakeAChoiceEntity>

    @Insert
    fun insertAll(vararg makeAChoiceEntity: MakeAChoiceEntity)

    @Delete
    fun delItem(vararg makeAChoiceEntity: MakeAChoiceEntity): Int
}