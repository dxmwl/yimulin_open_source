package com.yimulin.mobile.ui.activity

import androidx.appcompat.widget.AppCompatTextView
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.other.AppConfig

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 关于界面
 */
class AboutActivity : AppActivity() {

    private val app_version: AppCompatTextView? by lazy { findViewById(R.id.app_version) }

    override fun getLayoutId(): Int {
        return R.layout.about_activity
    }

    override fun initView() {
        app_version?.text =
            "版本号:V${AppConfig.getVersionName()}-${AppConfig.getVersionCode()}"
    }

    override fun initData() {}
}