package com.yimulin.mobile.ui.fragment

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.hjq.shape.view.ShapeEditText
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment


/**
 * @ClassName: QQForcedConversationFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/12/26 0026 20:12
 **/
class QQForcedConversationFragment : AppFragment<AppActivity>() {

    private val qq_number: ShapeEditText? by lazy { findViewById(R.id.qq_number) }

    private var qqNumberValue = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_qq_forced_conversation
    }

    override fun initView() {
        setOnClickListener(R.id.btn_create)
        qq_number?.addTextChangedListener {
            qqNumberValue = it.toString()
        }
    }

    override fun initData() {

    }


    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_create -> {
                try {
                    val url = "mqqwpa://im/chat?chat_type=wpa&uin=${qqNumberValue}"
                    //uin是发送过去的qq号码
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (e: Exception) {
                    e.printStackTrace()
                    toast("您还没有安装QQ，请先安装软件")
                }
            }

            else -> {}
        }
    }
}