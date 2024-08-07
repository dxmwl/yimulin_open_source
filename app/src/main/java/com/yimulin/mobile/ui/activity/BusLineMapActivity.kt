package com.yimulin.mobile.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.LatLngBounds
import com.amap.api.maps.model.PolylineOptions
import com.amap.api.services.busline.BusLineQuery
import com.amap.api.services.busline.BusLineSearch
import com.blankj.utilcode.util.GsonUtils
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils
import com.orhanobut.logger.Logger
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppManager
import com.yimulin.mobile.other.PermissionInterceptor


/**
 * @project : Travel_without_worry
 * @Description : 项目描述
 * @author : 常利兵
 * @time : 2022/3/29
 */
class BusLineMapActivity : AppActivity() {

    private var aMap: AMap? = null
    private var busLineId: String? = null

    private val mapView: MapView? by lazy { findViewById(R.id.map_view) }

    override fun getLayoutId(): Int {
        return R.layout.activity_bus_line_map
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView?.onCreate(savedInstanceState)
    }

    override fun initView() {
        busLineId = intent.getStringExtra("busLineId")

        aMap = mapView?.map
    }

    override fun initData() {
        XXPermissions.with(this)
            .permission(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
            .interceptor(PermissionInterceptor("申请位置权限，用于确定您的位置，搜索附近的公交线路"))
            .request { permissions, all ->
                if (all) {
                    searchBusLineData()
                } else {
                    toast("未获取定位权限，请在设置中开启")
                }
            }
    }

    private fun searchBusLineData() {
        Logger.d("线路ID：${busLineId}")
        val busLineQuery = BusLineQuery(
            busLineId,
            BusLineQuery.SearchType.BY_LINE_ID,
            AppManager.locationInfo?.cityCode
        )
        busLineQuery.extensions = "all"//返回全部信息
        val busLineSearch = BusLineSearch(this, busLineQuery)
        busLineSearch.setOnBusLineSearchListener { busLineResult, rCode ->
            if (rCode == 1000) {
                Logger.d(GsonUtils.toJson(busLineResult))
                val busLineItem = busLineResult.busLines[0]
                busLineItem?.let {
                    val directionsCoordinates = it.directionsCoordinates
                    val linesPoint = arrayListOf<LatLng>()
                    val newbounds = LatLngBounds.Builder()
                    directionsCoordinates.forEachIndexed { _, latLonPoint ->
                        val latLng = LatLng(latLonPoint.latitude, latLonPoint.longitude)
                        linesPoint.add(latLng)
                        newbounds.include(latLng) //通过for循环将所有的轨迹点添加进去.
                    }
                    aMap?.addPolyline(
                        PolylineOptions().addAll(linesPoint).width(10f)
                            .setUseTexture(true)
                            .useGradient(true)
                            .width(15f)
                            .color(ContextCompat.getColor(this, R.color.common_accent_color))
                    )

                    aMap?.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(newbounds.build(), 30)
                    ) //第二个参数为四周留空宽度

                }
            } else {
                ToastUtils.show("当前线路信息获取失败")
            }
        }
        busLineSearch.searchBusLineAsyn()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}