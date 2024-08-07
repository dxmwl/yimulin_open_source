package com.yimulin.mobile.ui.fragment

import android.view.View
import android.widget.EditText
import com.blankj.utilcode.util.EncryptUtils
import com.yimulin.mobile.R

/**
 * @ClassName: Md5EncryptFragment
 * @Description: MD5加密
 * @Author: 常利兵
 * @Date: 2023/5/16 22:50
 **/
class Md5EncryptFragment: com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>() {

    private val input_content: EditText? by lazy { findViewById(R.id.input_content) }
    private val result_content: EditText? by lazy { findViewById(R.id.result_content) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_md5_encrypt
    }

    override fun initView() {
        setOnClickListener(R.id.btn_switch)
    }

    override fun initData() {
        
    }


    @com.yimulin.mobile.aop.SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btn_switch -> {
                val s = input_content?.text?.toString()
                if (s.isNullOrBlank()) {
                    toast("请输入要转换的内容")
                    return
                }
                result_content?.setText(EncryptUtils.encryptMD5ToString(s))
            }
            else -> {}
        }
    }
}