package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppAdapter
import com.yimulin.mobile.http.api.SearchCompanyBlackApi

/**
 * @ClassName: CompanyBlackListAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/7/31 15:17
 **/
class CompanyBlackListAdapter(val mContext:Context): AppAdapter<SearchCompanyBlackApi.CompanyBlackDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder:AppViewHolder(R.layout.item_company_black){

        private val textView:TextView? by lazy { findViewById(R.id.textView) }
        private val textView6:TextView? by lazy { findViewById(R.id.textView6) }
        private val textView7:TextView? by lazy { findViewById(R.id.textView7) }
        private val report_reason:TextView? by lazy { findViewById(R.id.report_reason) }

        override fun onBindView(position: Int) {
            val item = getItem(position)
            textView?.text = item.companyName
            textView6?.text = item.companyNumber
            textView7?.text = item.legalRepresentative
            report_reason?.text = item.reportType
        }
    }
}