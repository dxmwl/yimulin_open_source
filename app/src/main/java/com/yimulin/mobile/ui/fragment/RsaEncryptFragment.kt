package com.yimulin.mobile.ui.fragment

import android.view.View
import android.widget.EditText
import com.yimulin.mobile.R

/**
 * @ClassName: RsaEncryptFragment
 * @Description: RSA加密
 * @Author: 常利兵
 * @Date: 2023/5/19 15:21
 **/
class RsaEncryptFragment: com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>() {

    private val input_content: EditText? by lazy { findViewById(R.id.input_content) }
    private val result_content: EditText? by lazy { findViewById(R.id.result_content) }


    override fun getLayoutId(): Int {
        return R.layout.fragment_rsa_encrypt
    }

    override fun initView() {
        setOnClickListener(R.id.btn_jiami, R.id.btn_jiemi)
    }

    override fun initData() {
        
    }

    @com.yimulin.mobile.aop.SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
//            R.id.btn_jiami -> {
//                val s = input_content?.text?.toString()
//                if (s.isNullOrBlank()) {
//                    toast("请输入要转换的内容")
//                    return
//                }
//                result_content?.setText(EncryptUtils.encryptRSA2HexString(s))
//            }
//            R.id.btn_jiemi -> {
//                val s = result_content?.text?.toString()
//                if (s.isNullOrBlank()) {
//                    toast("请输入要转换的内容")
//                    return
//                }
//                input_content?.setText(EncryptUtils.decryptHexStringRSA(s))
//            }
            else -> {}
        }
    }
}