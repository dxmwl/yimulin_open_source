//package com.yimulin.mobile.ui.activity
//
//import android.content.Intent
//import android.view.View
//import android.widget.TextView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
////import com.amap.api.services.busline.BusLineItem
////import com.amap.api.services.busline.BusLineQuery
////import com.amap.api.services.busline.BusLineSearch
//import com.blankj.utilcode.util.GsonUtils
//import com.hjq.toast.ToastUtils
//import com.kwad.sdk.core.b.a.it
//import com.orhanobut.logger.Logger
//import com.pdlbox.tools.utils.DateUtils
//import com.yimulin.mobile.R
//import com.yimulin.mobile.app.AppActivity
//import com.yimulin.mobile.app.AppManager
//import com.yimulin.mobile.ui.adapter.BusLinePlatformListAdapter
//
///**
// * @project : Travel_without_worry
// * @Description : 项目描述
// * @author : 常利兵
// * @time : 2022/3/28
// */
//class BusLineActivity : AppActivity() {
//
//    private var busLineId: String? = null
//    private val busPlatformList: RecyclerView? by lazy { findViewById(R.id.bus_platform_list) }
//    private val direction: TextView? by lazy { findViewById(R.id.direction) }
//    private val startTime: TextView? by lazy { findViewById(R.id.start_time) }
//    private val endTime: TextView? by lazy { findViewById(R.id.end_time) }
//    private val price: TextView? by lazy { findViewById(R.id.price) }
//
//    private lateinit var busLinePlatformListAdapter: BusLinePlatformListAdapter
//
//
//    override fun getLayoutId(): Int {
//        return R.layout.activity_bus_line
//    }
//
//    override fun initView() {
//        setOnClickListener(R.id.view_in_map)
//        busLineId = intent.getStringExtra("busLineId")
//        busPlatformList?.also {
//            it.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//            busLinePlatformListAdapter = BusLinePlatformListAdapter(this)
//            it.adapter = busLinePlatformListAdapter
//        }
//
//    }
//
//    override fun initData() {
//        searchBusLineData()
//    }
//
//    private fun searchBusLineData() {
//        Logger.d("线路ID：${busLineId}")
//        val busLineQuery = BusLineQuery(
//            busLineId,
//            BusLineQuery.SearchType.BY_LINE_ID,
//            AppManager.locationInfo?.cityCode
//        )
//        busLineQuery.extensions = "all"//返回全部信息
//        val busLineSearch = BusLineSearch(this, busLineQuery)
//        busLineSearch.setOnBusLineSearchListener { busLineResult, rCode ->
//            if (rCode == 1000) {
//                Logger.d(GsonUtils.toJson(busLineResult))
//                val busLineItem = busLineResult.busLines[0]
//                busLineItem?.let {
//                    direction?.text = it.terminalStation
//                    startTime?.text =
//                        DateUtils.millis2String(it.firstBusTime.time, "hh:mm")
//                    endTime?.text = DateUtils.millis2String(it.lastBusTime.time, "hh:mm")
//                    price?.text = it.basicPrice.toString()
//                    busLinePlatformListAdapter.setData(it.busStations)
//                }
//            } else {
//                ToastUtils.show("当前线路信息获取失败")
//            }
//        }
//        busLineSearch.searchBusLineAsyn()
//    }
//
//    @com.yimulin.mobile.aop.SingleClick
//    override fun onClick(view: View) {
//        super.onClick(view)
//        when (view.id) {
//            R.id.view_in_map -> {
//                val intent = Intent(this, BusLineMapActivity::class.java)
//                intent.putExtra("busLineId",busLineId)
//                startActivity(intent)
//            }
//            else -> {}
//        }
//    }
//}