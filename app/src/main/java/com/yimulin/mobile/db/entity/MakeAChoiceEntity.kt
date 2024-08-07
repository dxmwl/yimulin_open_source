package com.yimulin.mobile.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @ClassName: MakeAChoiceEntity
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/8/11 19:54
 **/
@Entity(tableName = "makeAChoice")
data class MakeAChoiceEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "choice_title") val choiceTitle: String?
)
