package com.yimulin.mobile.ui.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.base.BaseAdapter
import com.hjq.base.livebus.LiveDataBus
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.http.api.SearchCompanyBlackApi
import com.yimulin.mobile.http.api.ZipCodeInquiryListApi
import com.yimulin.mobile.http.model.HttpData
import com.yimulin.mobile.ui.activity.CompanyBlackDetailActivity
import com.yimulin.mobile.ui.activity.ReportCompanyActivity
import com.yimulin.mobile.ui.adapter.CompanyBlackListAdapter
import java.lang.Exception

/**
 * @ClassName: CompanyBlackListFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/7/30 19:49
 **/
class CompanyBlackListFragment : AppFragment<AppActivity>(), BaseAdapter.OnItemClickListener {

    private lateinit var companyBlackListAdapter: CompanyBlackListAdapter
    private val company_black_list: RecyclerView? by lazy { findViewById(R.id.company_black_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private var pageNum = 1
    private var keyWord: String? = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_company_black_list
    }

    override fun initView() {
        setOnClickListener(R.id.report_company)

        company_black_list?.also {
            it.layoutManager = LinearLayoutManager(context)
            companyBlackListAdapter = CompanyBlackListAdapter(requireContext())
            companyBlackListAdapter.setOnItemClickListener(this)
            it.adapter = companyBlackListAdapter
        }

        refresh?.setOnRefreshListener {
            pageNum = 1
            searchCompanyBlack()
        }

        refresh?.setOnLoadMoreListener {
            pageNum++
            searchCompanyBlack()
        }
    }

    private fun searchCompanyBlack() {
        EasyHttp.post(this)
            .api(SearchCompanyBlackApi().apply {
                this.keyWord = this@CompanyBlackListFragment.keyWord
                this.pageNum = this@CompanyBlackListFragment.pageNum
            })
            .request(object :
                OnHttpListener<HttpData<ArrayList<SearchCompanyBlackApi.CompanyBlackDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<SearchCompanyBlackApi.CompanyBlackDto>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    val zipCodeDtos = result?.getData()
                    if (zipCodeDtos.isNullOrEmpty()) {
                        toast("查询内容为空，可能该公司信誉良好")
                        return
                    }
                    zipCodeDtos.let {
                        if (pageNum == 1) {
                            companyBlackListAdapter.setData(it)
                        } else {
                            companyBlackListAdapter.addData(it)
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

    override fun initData() {
        LiveDataBus.subscribe("search", this) { data ->
            keyWord = data as String
            pageNum = 1
            searchCompanyBlack()
        }
        searchCompanyBlack()
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.report_company -> {
                startActivity(ReportCompanyActivity::class.java)
            }
            else -> {}
        }
    }

    override fun onItemClick(recyclerView: RecyclerView?, itemView: View?, position: Int) {
        val item = companyBlackListAdapter.getItem(position)
        val intent = Intent(requireContext(), CompanyBlackDetailActivity::class.java)
        intent.putExtra("id", item.id)
        startActivity(intent)
    }
}