package com.yimulin.mobile.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ThreadUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.action.StatusAction
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.ui.adapter.AppManagerAdapter
import com.yimulin.mobile.widget.StatusLayout
import kotlin.concurrent.thread

/**
 * @ClassName: AppManagerFragment
 * @Description: 手机应用管理
 * @Author: 常利兵
 * @Date: 2023/6/21 11:24
 **/
class AppManagerFragment : AppFragment<AppActivity>(), StatusAction {

    private lateinit var appManagerAdapter: AppManagerAdapter
    private val app_list: RecyclerView? by lazy { findViewById(R.id.app_list) }
    private val status_layout: StatusLayout? by lazy { findViewById(R.id.status_layout) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_app_manager
    }

    override fun initView() {
        app_list?.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            appManagerAdapter = AppManagerAdapter(requireContext())
            it.adapter = appManagerAdapter
        }
    }

    override fun initData() {
        showLoading()
        thread {
            val appList = AppUtils.getAppsInfo()
            ThreadUtils.runOnUiThread {
                showComplete()
                appManagerAdapter.setData(appList)
            }
        }
    }

    override fun getStatusLayout(): StatusLayout? {
        return status_layout
    }
}