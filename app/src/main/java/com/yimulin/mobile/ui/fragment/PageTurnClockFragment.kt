package com.yimulin.mobile.ui.fragment

import android.content.pm.ActivityInfo
import android.view.WindowManager
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.http.model.TimeTAG
import com.yimulin.mobile.widget.FlipLayout
import java.util.*

/**
 * @ClassName: PageTurnClockFragment
 * @Description: 翻页时钟
 * @Author: 常利兵
 * @Date: 2023/6/23 13:06
 **/
class PageTurnClockFragment : AppFragment<AppActivity>(), FlipLayout.FlipOverListener {

    private var oldNumber: Calendar = Calendar.getInstance()
    private val bit_second: FlipLayout? by lazy { findViewById(R.id.bit_flip_3) }
    private val bit_minute: FlipLayout? by lazy { findViewById(R.id.bit_flip_2) }
    private val bit_hour: FlipLayout? by lazy { findViewById(R.id.bit_flip_1) }

    override fun getLayoutId(): Int {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR)
        //在Window增加flag打开屏幕常亮：
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        return R.layout.fragment_page_turn_colock
    }

    override fun initView() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init()
        bit_hour?.flip(oldNumber.get(Calendar.HOUR_OF_DAY), 24, TimeTAG.hour)
        bit_minute?.flip(oldNumber.get(Calendar.MINUTE), 60, TimeTAG.min)
        bit_second?.flip(oldNumber.get(Calendar.SECOND), 60, TimeTAG.sec)


        Timer().schedule(object : TimerTask() {
            override fun run() {
                start()
            }
        }, 1000, 1000) //每一秒执行一次

//        bit_hour.addFlipOverListener(this);
//        bit_minute.addFlipOverListener(this);
//        bit_second.addFlipOverListener(this);
    }

    override fun initData() {

        start()
    }


    override fun onFLipOver(flipLayout: FlipLayout?) {
//        if(flipLayout.isFlipping()){
//            flipLayout.smoothFlip(1, true);
//        }
    }

    private fun start() {
        val now = Calendar.getInstance()
        val nhour = now[Calendar.HOUR_OF_DAY]
        val nminute = now[Calendar.MINUTE]
        val nsecond = now[Calendar.SECOND]
        val ohour: Int = oldNumber.get(Calendar.HOUR_OF_DAY)
        val ominute: Int = oldNumber.get(Calendar.MINUTE)
        val osecond: Int = oldNumber.get(Calendar.SECOND)
        oldNumber = now
        val hour = nhour - ohour
        val minute = nminute - ominute
        val second = nsecond - osecond
        if (hour >= 1 || hour == -23) {
            bit_hour?.smoothFlip(1, 24, TimeTAG.hour, false)
        }
        if (minute >= 1 || minute == -59) {
            bit_minute?.smoothFlip(1, 60, TimeTAG.min, false)
        }
        if (second >= 1 || second == -59) {
            bit_second?.smoothFlip(1, 60, TimeTAG.sec, false)
        } //当下一秒变为0时减去上一秒是-59
    }
}