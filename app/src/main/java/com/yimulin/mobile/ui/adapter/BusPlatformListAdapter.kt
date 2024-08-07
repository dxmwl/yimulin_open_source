package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.amap.api.services.busline.BusStationQuery
import com.amap.api.services.busline.BusStationSearch
import com.amap.api.services.core.PoiItem
import com.hjq.base.livebus.LiveDataBus
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppAdapter
import com.yimulin.mobile.app.AppManager
import com.yimulin.mobile.ui.activity.BusLineActivity

/**
 * @project : Travel_without_worry
 * @Description : 项目描述
 * @author : 常利兵
 * @time : 2022/3/28
 */
class BusPlatformListAdapter(val mContext: Context) : AppAdapter<PoiItem>(mContext) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_bus_platform) {

        private val busPlatformName: TextView? by lazy { findViewById(R.id.bus_platform_name) }
        private val distanceTv: TextView? by lazy { findViewById(R.id.distance_tv) }
        private val busLineList: LinearLayout? by lazy { findViewById(R.id.bus_line_list) }

        override fun onBindView(position: Int) {

            val item = getItem(position)

            busPlatformName?.text = item.title
            val locationInfo = AppManager.locationInfo
            distanceTv?.text = if (locationInfo != null) {
                val calculateLineDistance = AMapUtils.calculateLineDistance(
                    LatLng(item.latLonPoint.latitude, item.latLonPoint.longitude),
                    LatLng(locationInfo.latitude, locationInfo.longitude)
                ).toInt()
                "${calculateLineDistance}米"
            } else {
                "-米"
            }

            searchBusLine(item)
        }

        /**
         * 查询公交线路
         */
        private fun searchBusLine(item: PoiItem) {
            val query = BusStationQuery(item.title, item.cityCode)
            val busStationSearch = BusStationSearch(mContext, query)
            busStationSearch.setOnBusStationSearchListener { busStationResult, rCode ->
                if (rCode == 1000) {
                    busLineList?.removeAllViews()
                    busStationResult.busStations[0].busLineItems.forEachIndexed { index, busLineItem ->
                        val busBinding =
                            LayoutInflater.from(mContext).inflate(R.layout.item_bus, null)
                        val busName: TextView? =busBinding.findViewById(R.id.bus_name)
                        val directionTv: TextView?= busBinding.findViewById(R.id.direction_tv)

                        busName?.text = busLineItem.busLineName.split("(")[0]
                        directionTv?.text = "方向 ${busLineItem.terminalStation}"
                        busLineList?.addView(busBinding)
                        busBinding.setOnClickListener {
                            val intent = Intent(it.context, BusLineActivity::class.java)
                            intent.putExtra("busLineId", busLineItem.busLineId)
                            it.context.startActivity(intent)
                        }
                    }
                    LiveDataBus.postValue("refreshBusLine",busStationResult.busStations[0].busLineItems)

                }
            }
            busStationSearch.searchBusStationAsyn()
        }
    }
}