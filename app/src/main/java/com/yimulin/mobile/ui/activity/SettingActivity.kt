package com.yimulin.mobile.ui.activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.view.Gravity
import android.view.View
import android.widget.DatePicker
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.SPUtils
import com.hjq.base.BaseDialog
import com.hjq.base.action.AnimAction
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.http.listener.OnHttpListener
import com.hjq.widget.layout.SettingBar
import com.hjq.widget.view.SwitchButton
import com.pdlbox.tools.utils.SpUtils
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.R
import com.yimulin.mobile.http.api.GetNewAppInfoApi
import com.yimulin.mobile.http.glide.GlideApp
import com.yimulin.mobile.http.model.HttpData
import com.yimulin.mobile.manager.ActivityManager
import com.yimulin.mobile.manager.CacheDataManager
import com.yimulin.mobile.manager.UserManager
import com.yimulin.mobile.other.AppConfig
import com.yimulin.mobile.ui.dialog.MenuDialog
import com.yimulin.mobile.ui.dialog.UpdateDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/03/01
 *    desc   : 设置界面
 */
class SettingActivity : AppActivity(), SwitchButton.OnCheckedChangeListener {

    private val languageView: SettingBar? by lazy { findViewById(R.id.sb_setting_language) }
    private val phoneView: SettingBar? by lazy { findViewById(R.id.sb_setting_phone) }
    private val passwordView: SettingBar? by lazy { findViewById(R.id.sb_setting_password) }
    private val cleanCacheView: SettingBar? by lazy { findViewById(R.id.sb_setting_cache) }
    private val autoSwitchView: SwitchButton? by lazy { findViewById(R.id.sb_setting_switch) }
    private val sb_push_gxh: SwitchButton? by lazy { findViewById(R.id.sb_push_gxh) }
    private val sb_setting_exit: SettingBar? by lazy { findViewById(R.id.sb_setting_exit) }

    override fun getLayoutId(): Int {
        return R.layout.setting_activity
    }

    override fun initView() {
        // 设置切换按钮的监听
        autoSwitchView?.setOnCheckedChangeListener(this)
        sb_push_gxh?.setOnCheckedChangeListener(this)

        setOnClickListener(
            R.id.sb_setting_language, R.id.sb_setting_update, R.id.sb_setting_phone,
            R.id.sb_setting_password, R.id.sb_setting_agreement, R.id.sb_setting_about,
            R.id.sb_setting_cache, R.id.sb_setting_exit, R.id.sb_setting_user_agreement,
            R.id.cancel_account,
        )

        autoSwitchView?.setChecked(SpUtils.getBoolean("APP_CONFIG", "PUSH_SWITCH", true))
        sb_push_gxh?.setChecked(SpUtils.getBoolean("APP_CONFIG", "PUSH_SWITCH_GXH", true))

        if (UserManager.userInfo == null) {
            sb_setting_exit?.visibility = View.GONE
        }
    }

    override fun initData() {
        val phoneNumber = UserManager.userInfo?.phonenumber.toString()
        // 获取应用缓存大小
        cleanCacheView?.setRightText(CacheDataManager.getTotalCacheSize(this))
        languageView?.setRightText("简体中文")
        phoneView?.setRightText(
            String.format(
                "%s****%s", phoneNumber.substring(0, 3),
                phoneNumber.substring(phoneNumber.length - 4)
            )
        )
        passwordView?.setRightText("密码强度较低")
    }

    /**
     * [SwitchButton.OnCheckedChangeListener]
     */
    override fun onCheckedChanged(button: SwitchButton, checked: Boolean) {
        when (button) {
            autoSwitchView -> {
//                if (checked) {
//                    PushAgent.getInstance(this).enable(null)
//                } else {
//                    PushAgent.getInstance(this).disable(null)
//                }
                SpUtils.put("APP_CONFIG", "PUSH_SWITCH", autoSwitchView?.isChecked() == true)
            }

            sb_push_gxh -> {
                SpUtils.put("APP_CONFIG", "PUSH_SWITCH_GXH", sb_push_gxh?.isChecked() == true)
            }

            else -> {}
        }
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.sb_setting_language -> {

                // 底部选择框
                MenuDialog.Builder(this) // 设置点击按钮后不关闭对话框
                    //.setAutoDismiss(false)
                    .setList(R.string.setting_language_simple, R.string.setting_language_complex)
                    .setListener(object : MenuDialog.OnListener<String> {

                        override fun onSelected(dialog: BaseDialog?, position: Int, data: String) {
                            languageView?.setRightText(data)
                            BrowserActivity.start(
                                this@SettingActivity,
                                "https://github.com/getActivity/MultiLanguages"
                            )
                        }
                    })
                    .setGravity(Gravity.BOTTOM)
                    .setAnimStyle(AnimAction.ANIM_BOTTOM)
                    .show()
            }

            R.id.sb_setting_user_agreement -> {

                BrowserActivity.start(this, AppConfig.getUserAgreement())
            }

            R.id.sb_setting_agreement -> {

                BrowserActivity.start(this, AppConfig.getPrivacyPolicy())
            }

            R.id.sb_setting_about -> {

                startActivity(AboutActivity::class.java)
            }

            R.id.sb_setting_cache -> {

                // 清除内存缓存（必须在主线程）
                GlideApp.get(this@SettingActivity).clearMemory()
                lifecycleScope.launch(Dispatchers.IO) {
                    CacheDataManager.clearAllCache(this@SettingActivity)
                    // 清除本地缓存（必须在子线程）
                    GlideApp.get(this@SettingActivity).clearDiskCache()
                    withContext(Dispatchers.Main) {
                        // 重新获取应用缓存大小
                        cleanCacheView?.setRightText(CacheDataManager.getTotalCacheSize(this@SettingActivity))
                    }
                }
            }

            R.id.sb_setting_exit -> {
                startActivity(LoginActivity::class.java)
                // 进行内存优化，销毁除登录页之外的所有界面
                ActivityManager.getInstance().finishAllActivities(
                    LoginActivity::class.java
                )
                UserManager.cleanToken()
            }

            R.id.cancel_account -> {
                startActivity(CancelAccountActivity::class.java)
            }

            R.id.sb_setting_update -> {
                getNewAppInfo()
                // 本地的版本码和服务器的进行比较
//                if (20 > AppConfig.getVersionCode()) {
//                    UpdateDialog.Builder(this)
//                        .setVersionName("2.0")
//                        .setForceUpdate(false)
//                        .setUpdateLog("修复Bug\n优化用户体验")
//                        .setDownloadUrl("https://down.qq.com/qqweb/QQ_1/android_apk/Android_8.5.0.5025_537066738.apk")
//                        .setFileMd5("560017dc94e8f9b65f4ca997c7feb326")
//                        .show()
//                } else {
//                    toast(R.string.update_no_update)
//                }
            }
        }
    }

    private fun getNewAppInfo() {
        EasyHttp.post(this)
            .api(GetNewAppInfoApi())
            .request(object : OnHttpListener<HttpData<GetNewAppInfoApi.NewAppInfoDto>> {
                override fun onSucceed(result: HttpData<GetNewAppInfoApi.NewAppInfoDto>?) {
                    result?.getData()?.let {

                        // 本地的版本码和服务器的进行比较
                        if (it.versionCode > AppConfig.getVersionCode()) {
                            UpdateDialog.Builder(this@SettingActivity)
                                .setVersionName(it.versionName)
                                .setForceUpdate(it.forceUpdate)
                                .setUpdateLog(it.updateContent)
                                .setDownloadUrl(it.downloadUrl)
//                                .setFileMd5("560017dc94e8f9b65f4ca997c7feb326")
                                .show()
                        } else {
                            toast(R.string.update_no_update)
                        }
                    }

                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }
            })
    }
}