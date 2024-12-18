//package com.yimulin.mobile.ui.fragment
//
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.amap.api.services.poisearch.PoiResult
//import com.hjq.base.livebus.LiveDataBus
//import com.yimulin.mobile.R
//import com.yimulin.mobile.ui.adapter.BusPlatformListAdapter
//import com.yimulin.mobile.app.AppActivity
//import com.yimulin.mobile.app.AppFragment
//
///**
// * @project : Travel_without_worry
// * @Description : 项目描述
// * @author : 常利兵
// * @time : 2022/3/28
// */
//class BusPlatformListFragment : AppFragment<AppActivity>() {
//
//    private lateinit var busPlatformListAdapter: BusPlatformListAdapter
//    private val recyclerView:RecyclerView? by lazy { findViewById(R.id.recyclerView) }
//
//    override fun getLayoutId(): Int {
//        return R.layout.fragment_bus_platform_list
//    }
//
//    override fun initView() {
//
//        recyclerView?.also {
//            it.layoutManager = LinearLayoutManager(context)
//            busPlatformListAdapter = BusPlatformListAdapter(requireContext())
//            it.adapter = busPlatformListAdapter
//        }
//
//        LiveDataBus.subscribe("poiResult",this){data->
//            val poiResult = data as PoiResult
//            busPlatformListAdapter.setData(poiResult.pois)
//        }
//    }
//
//    override fun initData() {
//
//    }
//}