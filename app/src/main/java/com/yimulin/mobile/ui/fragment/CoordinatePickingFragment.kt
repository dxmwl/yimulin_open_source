package com.yimulin.mobile.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.MyLocationStyle
import com.blankj.utilcode.util.ClipboardUtils
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.other.PermissionInterceptor


/**
 * @ClassName: CoordinatePickingFragment
 * @Description: 坐标拾取
 * @Author: 常利兵
 * @Date: 2023/6/24 20:49
 **/
class CoordinatePickingFragment : AppFragment<AppActivity>(), AMap.OnCameraChangeListener {

    private var aMap: AMap? = null
    private val location_info: TextView? by lazy { findViewById(R.id.location_info) }
    private val mapView: MapView? by lazy { findViewById(R.id.map_view) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_coordinate_picking
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView?.onCreate(savedInstanceState)
    }

    override fun initView() {

        setOnClickListener(R.id.btn_copy)

        aMap = mapView?.map
        XXPermissions.with(this)
            .permission(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
            .interceptor(PermissionInterceptor())
            .request { permissions, all ->
                if (all) {
                    initMap()
                } else {
                    toast("未获取定位权限，请在设置中开启")
                }
            }
    }

    private lateinit var myLocationStyle: MyLocationStyle

    private fun initMap() {
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = MyLocationStyle()

        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000)

        //定位一次，且将视角移动到地图中心点
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)

        //设置定位蓝点的Style
        aMap?.myLocationStyle = myLocationStyle

        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap?.isMyLocationEnabled = true

        aMap?.addOnCameraChangeListener(this)
    }

    override fun initData() {

    }

    @SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btn_copy -> {
                toast("已复制到剪切板")
                ClipboardUtils.copyText(location_info?.text.toString())
            }
            else -> {}
        }
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
        aMap?.removeOnCameraChangeListener(this)
        mapView?.onDestroy()
    }

    override fun onCameraChange(p0: CameraPosition?) {

    }

    override fun onCameraChangeFinish(p0: CameraPosition?) {
        p0?.target?.let {
            location_info?.text = "${it.latitude}，${it.longitude}"
        }
    }
}