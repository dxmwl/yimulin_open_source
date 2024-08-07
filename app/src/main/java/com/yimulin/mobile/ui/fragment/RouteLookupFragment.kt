package com.yimulin.mobile.ui.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.google.android.material.tabs.TabLayout
import com.hjq.base.livebus.LiveDataBus
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.orhanobut.logger.Logger
import com.yimulin.mobile.R
import com.yimulin.mobile.other.PermissionInterceptor
import com.yimulin.mobile.ui.adapter.MainFragmentAdapter
import kotlin.collections.ArrayList

/**
 * @ClassName: RouteLookupFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/6/17 16:59
 **/
class RouteLookupFragment: com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>(), PoiSearch.OnPoiSearchListener {

    private val tabLayoutList:TabLayout? by lazy { findViewById(R.id.tab_list) }
    private val vp:ViewPager? by lazy { findViewById(R.id.vp) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_route_lookup
    }

    override fun initView() {
        val tabList = listOf("站台", "公交车")
        val fragments = ArrayList<Fragment>()
        fragments.add(BusPlatformListFragment())
        fragments.add(BusListFragment())
        tabLayoutList?.setupWithViewPager(vp)
        vp?.adapter =
            MainFragmentAdapter(childFragmentManager, fragments, tabList)
        getLocationPermission()
    }

    override fun initData() {

    }

    private fun getLocationPermission() {
        XXPermissions.with(this)
            .permission(
                Permission.ACCESS_COARSE_LOCATION,
                Permission.ACCESS_BACKGROUND_LOCATION,
                Permission.ACCESS_FINE_LOCATION
            )
            .interceptor(PermissionInterceptor("申请位置权限，用于确定您的位置，搜索附近的公交线路"))
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                    startLocation()
                }

                override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                    super.onDenied(permissions, never)

                }
            })
    }

    private fun startLocation() {
        val locationClient = AMapLocationClient(requireContext())
        val option = AMapLocationClientOption()
        option.interval = 60 * 1000
        locationClient.setLocationOption(option)
        locationClient.setLocationListener {
            if (it.errorCode == 0) {
                com.yimulin.mobile.app.AppManager.locationInfo = it
                searchBusPlatform(it)
            }
            Logger.d(it)
        }
        locationClient.startLocation()
    }

    /**
     * 搜索公交站台
     */
    private fun searchBusPlatform(aMapLocation: AMapLocation) {
        val query = PoiSearch.Query("", "150700", aMapLocation.cityCode)
//keyWord表示搜索字符串，
//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.pageSize = 10 // 设置每页最多返回多少条poiitem
        query.pageNum = 1 //设置查询页码
        val poiSearch = PoiSearch(requireContext(), query)
        poiSearch.bound = PoiSearch.SearchBound(
            LatLonPoint(
                aMapLocation.latitude,
                aMapLocation.longitude
            ), 1000
        ) //设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(this)
        poiSearch.searchPOIAsyn()
    }

    override fun onPoiSearched(p0: PoiResult?, p1: Int) {
        p0?.let { LiveDataBus.postValue("poiResult", it) }
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {

    }
}