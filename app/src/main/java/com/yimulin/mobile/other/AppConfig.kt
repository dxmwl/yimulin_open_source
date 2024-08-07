package com.yimulin.mobile.other

import android.content.pm.PackageManager
import com.yimulin.mobile.BuildConfig
import com.yimulin.mobile.app.AppApplication

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/09/02
 *    desc   : App 配置管理类
 */
object AppConfig {

    /**
     * 当前是否为调试模式
     */
    fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    /**
     * 获取当前构建的模式
     */
    fun getBuildType(): String {
        return BuildConfig.BUILD_TYPE
    }

    /**
     * 当前是否要开启日志打印功能
     */
    fun isLogEnable(): Boolean {
        return BuildConfig.LOG_ENABLE
    }

    /**
     * 获取当前应用的包名
     */
    fun getPackageName(): String {
        return BuildConfig.APPLICATION_ID
    }

    /**
     * 获取当前应用的版本名
     */
    fun getVersionName(): String {
        return BuildConfig.VERSION_NAME
    }

    /**
     * 获取当前应用的版本码
     */
    fun getVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }

    /**
     * 获取 Bugly Id
     */
    fun getBuglyId(): String {
        return BuildConfig.BUGLY_ID
    }

    /**
     * 获取服务器主机地址
     */
    fun getHostUrl(): String {
        return BuildConfig.HOST_URL
    }

    /**
     * 获取隐私政策
     */
    fun getPrivacyPolicy(): String {
        return "https://admin.youquan.dxmwl.com/articleDetail/11"
    }

    /**
     * 获取用户协议
     */
    fun getUserAgreement(): String {
        return "https://admin.youquan.dxmwl.com/articleDetail/10"
    }

    /**
     * 获取存储桶
     */
    fun getBucketName(): String {
        return BuildConfig.BUCKET_NAME
    }

    /**
     * 获取渠道
     */
    fun getChannel(): String {
        var channelName = "guanfang"
        try {
            val packageManager = AppApplication.getApplication().packageManager;
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                val applicationInfo = packageManager.getApplicationInfo(
                    AppApplication.getApplication().packageName,
                    PackageManager.GET_META_DATA
                );
                if (applicationInfo.metaData != null) {
                    channelName = applicationInfo.metaData.getString("UMENG_CHANNEL") ?: "guanfang"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return channelName;
    }

}