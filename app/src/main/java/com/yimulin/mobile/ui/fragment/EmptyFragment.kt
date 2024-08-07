package com.yimulin.mobile.ui.fragment

import com.yimulin.mobile.R
import com.yimulin.mobile.action.StatusAction
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.widget.StatusLayout

/**
 * @ClassName: EmptyFragment
 * @Description: 空页面
 * @Author: 常利兵
 * @Date: 2023/5/12 22:07
 **/
class EmptyFragment: AppFragment<AppActivity>() , StatusAction {

    private val status_layout: StatusLayout? by lazy { findViewById(R.id.status_layout) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_empty
    }

    override fun initView() {
        showEmpty()
    }

    override fun initData() {
        
    }

    override fun getStatusLayout(): StatusLayout? {
        return status_layout
    }
}