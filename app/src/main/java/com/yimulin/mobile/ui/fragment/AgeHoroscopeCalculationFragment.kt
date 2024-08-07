package com.yimulin.mobile.ui.fragment

import android.view.View
import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.utils.BirthdayUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * @ClassName: AgeHoroscopeCalculation
 * @Description: 年龄星座计算
 * @Author: 常利兵
 * @Date: 2023/6/3 11:03
 **/
class AgeHoroscopeCalculationFragment : AppFragment<AppActivity>() {

    private val user_birthday: TextView? by lazy { findViewById(R.id.user_birthday) }
    private val tv_age: TextView? by lazy { findViewById(R.id.tv_age) }
    private val tv_xingzuo: TextView? by lazy { findViewById(R.id.tv_xingzuo) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_age_horoscope_calculation
    }

    override fun initView() {
        setOnClickListener(R.id.layout_choose_birthday)
    }

    override fun initData() {
        
    }

    @com.yimulin.mobile.aop.SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.layout_choose_birthday -> {
                //时间选择器

                //时间选择器
                TimePickerBuilder(requireContext()) { date, v ->
                    // 如果不指定年月日则默认为今天的日期
                    val calendar: Calendar = Calendar.getInstance()
                    calendar.time = date
                    val format = SimpleDateFormat("yyyy-MM-dd").format(
                        calendar.time
                    )
                    user_birthday?.text = format

                    //计算星座
                    tv_xingzuo?.text =
                        BirthdayUtils.calculateTheConstellations(calendar.time.time)
                    //计算年龄
                    tv_age?.text = BirthdayUtils.calculateAge(calendar.time.time).toString()
                }.build().show()
            }
        }
    }
}