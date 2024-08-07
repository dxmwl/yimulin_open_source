package com.yimulin.mobile.ui.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.pdlbox.tools.utils.ClipboardUtils
import com.pdlbox.tools.utils.PinyinUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment

/**
 * @ClassName: ChineseTurnPinyinFragment
 * @Description: 中文转拼音
 * @Author: 常利兵
 * @Date: 2023/5/15 22:30
 **/
class ChineseTurnPinyinFragment : AppFragment<AppActivity>() {

    private val input_content: EditText? by lazy { findViewById(R.id.input_content) }
    private val tv_1: TextView? by lazy { findViewById(R.id.tv_1) }
    private val tv_2: TextView? by lazy { findViewById(R.id.tv_2) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_chinese_turn_pinyin
    }

    override fun initView() {
        setOnClickListener(R.id.btn_copy_1, R.id.btn_copy_2)

        input_content?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return
                try {
                    tv_1?.text = PinyinUtils.getPingYin(s.toString())
                    tv_2?.text = PinyinUtils.getFirstSpell(s.toString())
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
            R.id.btn_copy_1 -> {
                toast("复制成功")
                ClipboardUtils.copyText(tv_1?.text.toString())
            }
            R.id.btn_copy_2 -> {
                toast("复制成功")
                ClipboardUtils.copyText(tv_2?.text.toString())
            }
            else -> {}
        }
    }
}