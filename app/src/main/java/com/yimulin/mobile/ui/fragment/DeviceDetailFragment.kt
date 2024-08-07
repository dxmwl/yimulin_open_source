package com.yimulin.mobile.ui.fragment

import android.widget.TextView
import com.blankj.utilcode.util.DeviceUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment

/**
 * @ClassName: DeviceDetailFragment
 * @Description: 设备详情
 * @Author: 常利兵
 * @Date: 2023/5/12 21:53
 **/
class DeviceDetailFragment : AppFragment<AppActivity>() {

    private val system_version:TextView? by lazy { findViewById(R.id.system_version) }
    private val system_version_code:TextView? by lazy { findViewById(R.id.system_version_code) }
    private val android_id:TextView? by lazy { findViewById(R.id.android_id) }
    private val abis:TextView? by lazy { findViewById(R.id.abis) }
    private val mac_address:TextView? by lazy { findViewById(R.id.mac_address) }
    private val is_root:TextView? by lazy { findViewById(R.id.is_root) }
    private val adb_enable:TextView? by lazy { findViewById(R.id.adb_enable) }
    private val changshang:TextView? by lazy { findViewById(R.id.changshang) }
    private val xinghao:TextView? by lazy { findViewById(R.id.xinghao) }
    private val is_pingban:TextView? by lazy { findViewById(R.id.is_pingban) }
    private val is_moniqi:TextView? by lazy { findViewById(R.id.is_moniqi) }
    private val dev_enable:TextView? by lazy { findViewById(R.id.dev_enable) }
    private val device_id:TextView? by lazy { findViewById(R.id.device_id) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_device_detail
    }

    override fun initView() {
        system_version?.text = DeviceUtils.getSDKVersionName()
        system_version_code?.text = "${DeviceUtils.getSDKVersionCode()}"
        android_id?.text = "${DeviceUtils.getAndroidID()}"
        abis?.text = "${DeviceUtils.getABIs().toList()}"
        mac_address?.text = "${DeviceUtils.getMacAddress()}"
        is_root?.text = "${DeviceUtils.isDeviceRooted()}"
        adb_enable?.text = "${DeviceUtils.isAdbEnabled()}"
        changshang?.text = "${DeviceUtils.getManufacturer()}"
        xinghao?.text = "${DeviceUtils.getModel()}"
        is_pingban?.text = "${DeviceUtils.isTablet()}"
        is_moniqi?.text = "${DeviceUtils.isEmulator()}"
        dev_enable?.text = "${DeviceUtils.isDevelopmentSettingsEnabled()}"
        device_id?.text = "${DeviceUtils.getUniqueDeviceId()}"
    }

    override fun initData() {
    }

}