package com.yimulin.mobile.ui.fragment

import android.view.View
import android.widget.TextView
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.db.DbHelper
import com.yimulin.mobile.db.entity.MakeAChoiceEntity
import com.yimulin.mobile.ui.activity.EditChoiceActivity
import java.util.*
import kotlin.concurrent.thread


/**
 * @ClassName: MakeAChoiceFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/8/10 9:46
 **/
class MakeAChoiceFragment : AppFragment<AppActivity>() {

    private var mCurrentIndex = 0
    private val tv_result: TextView? by lazy { findViewById(R.id.tv_result) }
    private val shapeTextView2: TextView? by lazy { findViewById(R.id.shapeTextView2) }
    private var stopRandom = true

    override fun getLayoutId(): Int {
        return R.layout.fragment_make_a_choice
    }

    override fun initView() {
        setOnClickListener(R.id.shapeTextView2, R.id.edit_choice)
    }

    override fun initData() {

    }

    private var selectAllChoice: List<MakeAChoiceEntity>? = null

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.edit_choice -> {
                startActivity(EditChoiceActivity::class.java)
            }

            R.id.shapeTextView2 -> {
                //随机一个选项
                if (selectAllChoice == null || selectAllChoice?.isEmpty() == true) {
                    return
                }
                stopRandom = !stopRandom
                startRandom()
                if (stopRandom) {
                    shapeTextView2?.text = "开始"
                } else {
                    shapeTextView2?.text = "停止"
                }
            }

            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        initDataList()
    }

    private fun initDataList() {
        thread {
            //文字
            selectAllChoice = DbHelper.db.makeAChoiceDao().selectAllChoice()
        }
    }

    private fun startRandom() {
        if (stopRandom) {
            return
        }
        thread {
            while (!stopRandom) {
                displayText()
                try {
                    Thread.sleep(100) // 设置切换速度，单位为毫秒
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun displayText() {
        if (selectAllChoice == null || selectAllChoice?.isEmpty() == true) {
            return
        }
        tv_result?.text = selectAllChoice!!.get(mCurrentIndex).choiceTitle
        mCurrentIndex = (mCurrentIndex + 1) % selectAllChoice!!.size
    }

}