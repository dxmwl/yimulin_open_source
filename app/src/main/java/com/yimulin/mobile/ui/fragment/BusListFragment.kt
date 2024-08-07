package com.yimulin.mobile.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.services.busline.BusLineItem
import com.hjq.base.livebus.LiveDataBus
import com.yimulin.mobile.R
import com.yimulin.mobile.ui.adapter.BusListAdapter
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment

/**
 * @project : Travel_without_worry
 * @Description : 项目描述
 * @author : 常利兵
 * @time : 2022/3/28
 */
class BusListFragment : AppFragment<AppActivity>() {

    private lateinit var busListAdapter: BusListAdapter

    private val recyclerView:RecyclerView? by lazy { findViewById(R.id.recyclerView) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bus_list
    }

    override fun initView() {
        recyclerView?.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            busListAdapter = BusListAdapter(requireContext())
            it.adapter = busListAdapter
        }

        LiveDataBus.subscribe("refreshBusLine",this){data->
            busListAdapter.setData(data as  ArrayList<BusLineItem>)
        }
    }

    override fun initData() {
        
    }
}