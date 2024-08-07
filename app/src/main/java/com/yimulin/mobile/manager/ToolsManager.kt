package com.yimulin.mobile.manager

import android.app.Application
import com.blankj.utilcode.util.GsonUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.db.DbHelper
import com.yimulin.mobile.db.entity.MakeAChoiceEntity
import com.yimulin.mobile.db.entity.ToolsClassEntity
import com.yimulin.mobile.db.entity.ToolsEntity
import com.yimulin.mobile.db.entity.ToolsVersionEntity
import com.yimulin.mobile.http.model.ToolClassDto
import com.yimulin.mobile.http.model.ToolsBeanDto
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

/**
 * @ClassName: ToolsManager
 * @Description: 工具解析
 * @Author: 常利兵
 * @Date: 2023/5/19 17:39
 **/
object ToolsManager {

    var localTools = ArrayList<ToolClassDto>()
    var onlineTools = ArrayList<ToolClassDto>()

    fun init(app: Application) {
        thread {

            val inputStream = app.resources.openRawResource(R.raw.tools_data)
            val outStream = ByteArrayOutputStream()
            val buffer = ByteArray(512)
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                outStream.write(buffer, 0, length)
            }
            outStream.close()
            inputStream.close()
            val toolsBeanDto =
                GsonUtils.fromJson(outStream.toString(), ToolsBeanDto::class.java)

            thread {
                val all = DbHelper.db.toolsVersionDao().getAll()
                if (all.isEmpty()) {
                    resolveToolsData(toolsBeanDto)
                } else {
                    if (all[0].version!! < toolsBeanDto.version) {
                        DbHelper.db.toolsVersionDao().delete(all[0])
                    }
                    resolveToolsData(toolsBeanDto)
                }
            }

            initMakeAChoice()
        }

    }

    /**
     * 初始化做个决定
     */
    private fun initMakeAChoice() {
        val selectAllChoice = DbHelper.db.makeAChoiceDao().selectAllChoice()
        if (selectAllChoice.isEmpty().not()) {
            return
        }
        DbHelper.db.makeAChoiceDao().insertAll(
            MakeAChoiceEntity(null, choiceTitle = "玩游戏"),
            MakeAChoiceEntity(null, choiceTitle = "看电视"),
            MakeAChoiceEntity(null, choiceTitle = "逛街"),
            MakeAChoiceEntity(null, choiceTitle = "上班"),
            MakeAChoiceEntity(null, choiceTitle = "去旅游"),
            MakeAChoiceEntity(null, choiceTitle = "去骑行")
        )
    }

    /**
     * 解析数据
     */
    private fun resolveToolsData(toolsBeanDto: ToolsBeanDto) {
//        DbHelper.db.toolsVersionDao().insertAll(ToolsVersionEntity(null, toolsBeanDto.version))
        toolsBeanDto.data.forEach {
            if (it.isLocal) {
                localTools.add(it)
            } else {
                onlineTools.add(it)
            }

            val toolsClassEntity = ToolsClassEntity(
                null,
                classId = it.classId,
                className = it.className,
                isLocal = it.isLocal,
                sort = 0
            )
//            DbHelper.db.toolsClassDao().insertAll(toolsClassEntity)
            it.children.forEachIndexed { index, it1 ->
                val toolsEntity = ToolsEntity(
                    null,
                    classId = it.classId,
                    toolsId = it1.toolsId,
                    toolsName = it1.toolsName,
                    isFullScreen = it1.isFullScreen,
                    serviceType = it1.serviceType,
                    sort = 0,
                    url = it1.url,
                )
//                DbHelper.db.toolsDao().insertAll(toolsEntity)
            }
        }
    }
}