package com.yimulin.mobile.ui.fragment

import android.content.pm.ActivityInfo
import android.view.WindowManager
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment

/**
 * @ClassName: RulerFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/12/26 0026 14:18
 **/
class RulerFragment: AppFragment<AppActivity>() {
    override fun getLayoutId(): Int {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR)

        return R.layout.fragment_ruler
    }

    override fun initView() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init()
    }

    override fun initData() {
        
    }
}