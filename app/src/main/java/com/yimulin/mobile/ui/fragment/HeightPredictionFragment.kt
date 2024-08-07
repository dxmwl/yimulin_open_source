package com.yimulin.mobile.ui.fragment

import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.hjq.shape.view.ShapeEditText
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment

/**
 * @ClassName: HeightPredictionFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/12/26 0026 19:48
 **/
class HeightPredictionFragment : AppFragment<AppActivity>() {

    private val father_height: ShapeEditText? by lazy { findViewById(R.id.father_height) }
    private val mother_height: ShapeEditText? by lazy { findViewById(R.id.mother_height) }
    private val rg_sex: RadioGroup? by lazy { findViewById(R.id.rg_sex) }
    private val tv_result: TextView? by lazy { findViewById(R.id.tv_result) }

    private var fatherHeightValue = 0
    private var motherHeightValue = 0
    private var sex = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_height_prediction
    }

    override fun initView() {
        setOnClickListener(R.id.btn_create)

        father_height?.addTextChangedListener {
            if (it.isNullOrBlank().not()) {
                fatherHeightValue = it.toString().toInt()
            }
        }

        mother_height?.addTextChangedListener {
            if (it.isNullOrBlank().not()) {
                motherHeightValue = it.toString().toInt()
            }
        }
        rg_sex?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_nan -> {
                    sex = 0
                }

                R.id.rb_nv -> {
                    sex = 1
                }

                else -> {}
            }
        }

    }

    override fun initData() {

    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_create -> {
                when (sex) {
                    0 -> {
                        tv_result?.text = ((fatherHeightValue + motherHeightValue) * 1.08 / 2).toString()
                    }

                    1 -> {
                        tv_result?.text = ((fatherHeightValue * 0.923 + motherHeightValue) / 2).toString()
                    }

                    else -> {}
                }
            }

            else -> {}
        }
    }
}