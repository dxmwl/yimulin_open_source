package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.AppUtils.AppInfo
import com.blankj.utilcode.util.ClipboardUtils
import com.hjq.toast.ToastUtils
import com.makeramen.roundedimageview.RoundedImageView
import com.pdlbox.tools.utils.GlideUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppAdapter

/**
 * @ClassName: AppManagerAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/6/21 14:14
 **/
class AppManagerAdapter(val mContext: Context) : AppAdapter<AppInfo>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_app_info) {

        private val app_icon: RoundedImageView? by lazy { findViewById(R.id.app_icon) }
        private val app_name: TextView? by lazy { findViewById(R.id.app_name) }
        private val tv_package: TextView? by lazy { findViewById(R.id.tv_package) }
        private val tv_version_name: TextView? by lazy { findViewById(R.id.tv_version_name) }
        private val go_app_detail: TextView? by lazy { findViewById(R.id.go_app_detail) }
        private val copy_app_info: TextView? by lazy { findViewById(R.id.copy_app_info) }

        override fun onBindView(position: Int) {

            val appInfo = getItem(position)

            GlideUtils.showImg(appInfo.icon, app_icon)
            app_name?.text = appInfo.name
            tv_package?.text = appInfo.packageName
            tv_version_name?.text = appInfo.versionName

            go_app_detail?.setOnClickListener {
                AppUtils.launchAppDetailsSettings(appInfo.packageName)
            }
            copy_app_info?.setOnClickListener {
                ToastUtils.show("已复制到剪切板")
                ClipboardUtils.copyText(appInfo.toString())
            }
        }
    }
}