package com.yimulin.mobile.ui.fragment

import android.view.View
import android.widget.TextView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.orhanobut.logger.Logger
import com.pdlbox.tools.utils.ClipboardUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.other.PermissionInterceptor

/**
 * @ClassName: LocationInfoFragment
 * @Description: 定位信息获取
 * @Author: 常利兵
 * @Date: 2023/6/3 16:13
 **/
class LocationInfoFragment : com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>() {

    private val location_result: TextView? by lazy { findViewById(R.id.location_result) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_acquisition_of_location_information
    }

    override fun initView() {

        setOnClickListener(R.id.btn_copy,R.id.btn_location)
    }

    override fun initData() {

    }

    @com.yimulin.mobile.aop.SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btn_location->{
                getLocationInfo()
            }
            R.id.btn_copy -> {
                val toString = location_result?.text?.toString()
                if (toString.isNullOrEmpty()) {
                    toast("定位信息为空")
                    return
                }
                ClipboardUtils.copyText(toString)
                toast("已复制到剪切板")

            }
            else -> {}
        }
    }

    //声明AMapLocationClient类对象
    private var mLocationClient: AMapLocationClient? = null

    private fun getLocationInfo() {
        XXPermissions.with(this)
            .permission(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
            .interceptor(PermissionInterceptor("申请位置权限，用于确定您的位置"))
            .request { permissions, all ->
                if (all) {
                    //初始化定位
                    mLocationClient = AMapLocationClient(context)
                    //设置定位回调监听
                    mLocationClient?.setLocationListener(mAMapLocationListener)
                    //启动定位
                    mLocationClient?.startLocation()
                } else {
                    toast("未获取定位权限，请在设置中开启")
                }
            }
    }

    //异步获取定位结果
    private val mAMapLocationListener = AMapLocationListener { amapLocation ->
        if (amapLocation != null) {
            if (amapLocation.errorCode == 0) {
                //解析定位结果
                Logger.d(amapLocation)
                location_result?.text = "省份：${amapLocation.province}\n" +
                        "城市：${amapLocation.city}\n" +
                        "区/县：${amapLocation.district}\n" +
                        "详细地址：${amapLocation.address}\n" +
                        "经度：${amapLocation.latitude}\n" +
                        "纬度：${amapLocation.longitude}\n" +
                        "城市编码：${amapLocation.cityCode}\n" +
                        "地区编码：${amapLocation.adCode}\n" +
                        "road：${amapLocation.road}\n" +
                        "poiName：${amapLocation.poiName}\n" +
                        "street：${amapLocation.street}\n" +
                        "streetNum：${amapLocation.streetNum}\n" +
                        "aoiName：${amapLocation.aoiName}\n" +
                        "description：${amapLocation.description}\n"
            }
        }
    }
}