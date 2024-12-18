package com.yimulin.mobile.ui.fragment

import android.view.View
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.app.TitleBarFragment
import com.yimulin.mobile.other.AppConfig
import com.yimulin.mobile.ui.activity.BrowserActivity

/**
 *    author : 常利兵
 *    github : https://github.com/dxmwl
 *    time   : 2024/12/18 23:17
 *    file   : SettingFragment
 *    desc   : 设置
 */
class SettingFragment : TitleBarFragment<AppActivity>() {

    companion object {

        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun initView() {
        setOnClickListener(R.id.privacy_policy, R.id.sb_setting_user_agreement)
    }

    override fun initData() {

    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.privacy_policy -> {
                BrowserActivity.start(requireContext(), AppConfig.getPrivacyPolicy())
            }

            R.id.sb_setting_user_agreement -> {
                BrowserActivity.start(requireContext(), AppConfig.getUserAgreement())
            }

            else -> {}
        }
    }

    override fun isStatusBarEnabled(): Boolean {
        return !super.isStatusBarEnabled()
    }
}