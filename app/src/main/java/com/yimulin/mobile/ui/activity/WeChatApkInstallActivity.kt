package com.yimulin.mobile.ui.activity

import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.AppUtils
import com.hjq.permissions.Permission
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.Log
import com.yimulin.mobile.aop.Permissions
import com.yimulin.mobile.app.AppActivity


/**
 * @ClassName: WeChatApkInstallActivity
 * @Description:响应微信安装包安装
 * @Author: 常利兵
 * @Date: 2024/5/06 0006 23:28
 **/
class WeChatApkInstallActivity : AppActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_wechat_apk_install
    }

    override fun initView() {
        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {
            val apkUri = intent.data
            if (apkUri != null) {
                requestStoragePermission(apkUri)
            }
        }
    }

    @Log
    @Permissions(Permission.WRITE_EXTERNAL_STORAGE)
    private fun requestStoragePermission(apkUri: Uri) {
        AppUtils.installApp(apkUri)
    }

    override fun initData() {

    }
}