//package com.yimulin.mobile.ui.adapter
//
//import android.content.Context
//import android.graphics.Color
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import com.amap.api.services.busline.BusStationItem
//import com.yimulin.mobile.R
//import com.yimulin.mobile.app.AppAdapter
//
///**
// * @project : Travel_without_worry
// * @Description : 项目描述
// * @author : 常利兵
// * @time : 2022/3/29
// */
//class BusLinePlatformListAdapter(val mContext:Context) : AppAdapter<BusStationItem>(mContext) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
//return ViewHolder()
//    }
//
//    inner class ViewHolder:AppViewHolder(R.layout.item_bus_platform_detail){
//
//        private val platformName:TextView? by lazy { findViewById(R.id.platform_name) }
//        private val view1:View? by lazy { findViewById(R.id.view1) }
//        private val lineJiantou: ImageView? by lazy { findViewById(R.id.line_jiantou) }
//        private val lineStartOrEnd: TextView? by lazy { findViewById(R.id.line_start_or_end) }
//
//        override fun onBindView(position: Int) {
//            val item = getItem(position)
//
//            platformName?.text = item.busStationName
//
//            when (position) {
//                0 -> {
//                    //起点
//                    view1?.visibility = View.VISIBLE
//                    lineJiantou?.visibility = View.GONE
//                    lineStartOrEnd?.visibility = View.VISIBLE
//                    lineStartOrEnd?.text = "起"
//                    lineStartOrEnd?.setBackgroundColor(Color.parseColor("#4CAF50"))
//
//                }
//                (getData().size - 1) -> {
//                    //终点
//                    view1?.visibility = View.GONE
//                    lineJiantou?.visibility = View.GONE
//                    lineStartOrEnd?.visibility = View.VISIBLE
//                    lineStartOrEnd?.text = "终"
//                    lineStartOrEnd?.setBackgroundColor(Color.parseColor("#FF5722"))
//                }
//                else -> {
//                    //中间的站点
//                    view1?.visibility = View.VISIBLE
//                    lineJiantou?.visibility = View.VISIBLE
//                    lineStartOrEnd?.visibility = View.GONE
//                }
//            }
//        }
//    }
//}