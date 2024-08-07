package com.yimulin.mobile.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.TitleBarFragment
import com.yimulin.mobile.db.DbHelper
import com.yimulin.mobile.db.entity.ToolsClassEntity
import com.yimulin.mobile.db.entity.ToolsEntity
import com.yimulin.mobile.db.entity.ToolsVersionEntity
import com.yimulin.mobile.http.model.ToolsBeanDto
import com.yimulin.mobile.manager.ToolsManager
import com.yimulin.mobile.ui.adapter.ToolClassAdapter
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

/**
 * @ClassName: LocalToolsFragment
 * @Description: 在线工具
 * @Author: 常利兵
 * @Date: 2023/5/11 22:43
 **/
class OnlineToolsFragment : TitleBarFragment<AppActivity>() {

    companion object {

        fun newInstance(): OnlineToolsFragment {
            return OnlineToolsFragment()
        }
    }

    private lateinit var toolClassAdapter: ToolClassAdapter
    private val toolClassList: RecyclerView? by lazy { findViewById(R.id.tool_class_list) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_online_tools
    }

    override fun initView() {
        toolClassList?.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            toolClassAdapter = ToolClassAdapter(requireContext())
            it.adapter = toolClassAdapter
        }
    }

    override fun initData() {
        toolClassAdapter.setData(ToolsManager.onlineTools)
    }

    override fun isStatusBarEnabled(): Boolean {
        return !super.isStatusBarEnabled()
    }

}