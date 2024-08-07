package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.hjq.toast.ToastUtils
import com.pdlbox.tools.utils.ClipboardUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppAdapter
import com.yimulin.mobile.http.api.ZipCodeInquiryListApi

/**
 * @ClassName: ZipCodeListAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/6/1 15:34
 **/
class ZipCodeListAdapter(val mContext:Context): AppAdapter<ZipCodeInquiryListApi.ZipCodeDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder :AppViewHolder(R.layout.item_zip_code_inquiry){

        private val address_name:TextView? by lazy { findViewById(R.id.address_name) }
        private val zip_code:TextView? by lazy { findViewById(R.id.zip_code) }
        private val btn_copy:TextView? by lazy { findViewById(R.id.btn_copy) }

        override fun onBindView(position: Int) {

            val zipCodeDto = getItem(position)
            address_name?.text = zipCodeDto.addressName
            zip_code?.text = zipCodeDto.zipCode

            btn_copy?.setOnClickListener {
                ClipboardUtils.copyText(zipCodeDto.zipCode)
                ToastUtils.show("已复制到剪切板")
            }
        }
    }
}