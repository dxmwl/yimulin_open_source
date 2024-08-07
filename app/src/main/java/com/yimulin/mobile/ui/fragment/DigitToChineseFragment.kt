package com.yimulin.mobile.ui.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.pdlbox.tools.utils.ClipboardUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.utils.NumberUtils

/**
 * @ClassName: DigitToChineseFragment
 * @Description: 数字转中文
 * @Author: 常利兵
 * @Date: 2023/5/15 21:04
 **/
class DigitToChineseFragment : com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>() {

    private val input_content: EditText? by lazy { findViewById(R.id.input_content) }
    private val tv_1: TextView? by lazy { findViewById(R.id.tv_1) }
    private val tv_2: TextView? by lazy { findViewById(R.id.tv_2) }
    private val tv_3: TextView? by lazy { findViewById(R.id.tv_3) }
    private val tv_4: TextView? by lazy { findViewById(R.id.tv_4) }
    private val tv_5: TextView? by lazy { findViewById(R.id.tv_5) }
    private val tv_6: TextView? by lazy { findViewById(R.id.tv_6) }
    private val tv_7: TextView? by lazy { findViewById(R.id.tv_7) }
    private val tv_8: TextView? by lazy { findViewById(R.id.tv_8) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_digit_to_chinese
    }

    override fun initView() {

        setOnClickListener(R.id.btn_copy_3)

        input_content?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return
                try {
                    tv_3?.text = NumberUtils.upperRMB(s.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    override fun initData() {

    }

    @com.yimulin.mobile.aop.SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btn_copy_3 -> {
                toast("复制成功")
                ClipboardUtils.copyText(tv_3?.text.toString())
            }
            else -> {}
        }
    }
}