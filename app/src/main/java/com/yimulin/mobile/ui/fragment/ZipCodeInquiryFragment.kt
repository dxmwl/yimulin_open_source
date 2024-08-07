package com.yimulin.mobile.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.base.livebus.LiveDataBus
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yimulin.mobile.R
import com.yimulin.mobile.http.model.HttpData
import com.yimulin.mobile.ui.adapter.ZipCodeListAdapter
import java.lang.Exception

/**
 * @ClassName: ZipCodeInquiryFragment
 * @Description: 邮编查询
 * @Author: 常利兵
 * @Date: 2023/6/1 11:57
 **/
class ZipCodeInquiryFragment : com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>() {

    private lateinit var zipCodeListAdapter: ZipCodeListAdapter
    private var keyWord: String? = ""
    private val recyclerView:RecyclerView? by lazy { findViewById(R.id.recyclerView) }
    private val refresh:SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.fragment_zip_code_inquiry
    }

    override fun initView() {

        recyclerView?.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            zipCodeListAdapter = ZipCodeListAdapter(requireContext())
            it.adapter = zipCodeListAdapter
        }

        refresh?.setOnRefreshListener {
            pageNum = 1
            zipCodeInquiryList()
        }

        refresh?.setOnLoadMoreListener {
            pageNum++
            zipCodeInquiryList()
        }
    }

    override fun initData() {
        LiveDataBus.subscribe("search", this) { data ->
            keyWord = data as String
            pageNum = 1
            zipCodeInquiryList()
        }
    }

    private fun zipCodeInquiryList() {
        EasyHttp.post(this)
            .api(com.yimulin.mobile.http.api.ZipCodeInquiryListApi().apply {
                keyWord = this@ZipCodeInquiryFragment.keyWord
                pageNum = this@ZipCodeInquiryFragment.pageNum
            })
            .request(object :OnHttpListener<HttpData<ArrayList<com.yimulin.mobile.http.api.ZipCodeInquiryListApi.ZipCodeDto>>>{
                override fun onSucceed(result: HttpData<ArrayList<com.yimulin.mobile.http.api.ZipCodeInquiryListApi.ZipCodeDto>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    val zipCodeDtos = result?.getData()
                    if (zipCodeDtos.isNullOrEmpty()){
                        toast("查询内容为空，请输入正确的城市名称查询")
                        return
                    }
                    zipCodeDtos.let {
                        if (pageNum==1){
                            zipCodeListAdapter.setData(it)
                        }else{
                            zipCodeListAdapter.addData(it)
                        }
                    }
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }
            })
    }
}