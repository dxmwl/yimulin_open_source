package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.TextView
import com.amap.api.services.busline.BusLineItem
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppAdapter
import com.yimulin.mobile.ui.activity.BusLineActivity

/**
 * @project : Travel_without_worry
 * @Description : 项目描述
 * @author : 常利兵
 * @time : 2022/3/28
 */
class BusListAdapter(val mContext: Context) : AppAdapter<BusLineItem>(mContext) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_bus) {

        private val busName: TextView? by lazy { findViewById(R.id.bus_name) }
        private val directionTv: TextView? by lazy { findViewById(R.id.direction_tv) }

        override fun onBindView(position: Int) {

            val item = getItem(position)

            busName?.text = item.busLineName.split("(")[0]
            directionTv?.text = "方向 ${item.terminalStation}"

            getItemView().setOnClickListener {
                val intent = Intent(it.context, BusLineActivity::class.java)
                intent.putExtra("busLineId", item.busLineId)
                it.context.startActivity(intent)
            }
        }

    }
}