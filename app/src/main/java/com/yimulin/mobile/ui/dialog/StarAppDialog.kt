package com.yimulin.mobile.ui.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.hjq.base.BaseDialog
import com.hjq.base.action.AnimAction
import com.hjq.toast.ToastUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.other.AppConfig.getPackageName


/**
 * @ClassName: StartAppDialog
 * @Description: 应用评分弹窗
 * @Author: 常利兵
 * @Date: 2023/6/20 18:24
 **/
class StarAppDialog{

    class Builder(context: Context) : BaseDialog.Builder<Builder>(context) {
        init {
            setContentView(R.layout.dialog_star_app)
            setAnimStyle(AnimAction.ANIM_BOTTOM)

            setOnClickListener(R.id.to_market)
        }

        @SingleClick
        override fun onClick(view: View) {
            super.onClick(view)
            when (view.id) {
                R.id.to_market -> {
                    toMarket()
                    dismiss()
                }
                else -> {}
            }
        }

        private fun toMarket() {
            try {
                val uri: Uri = Uri.parse("market://details?id=" + getPackageName())
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                ToastUtils.show("您的手机没有安装Android应用市场")
                e.printStackTrace()
            }
        }
    }
}