package com.yimulin.mobile.ui.fragment

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import com.dyzs.compassservant.CompassServant
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment


/**
 * @ClassName: DecibelMeterFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/12/26 0026 19:07
 **/
class DecibelMeterFragment : AppFragment<AppActivity>(), CompassServant.ServantListener {

    private val compass_servant: CompassServant? by lazy { findViewById(R.id.compass_servant) }
    private var mHandlerThread: HandlerThread? = null
    private val mHtName = "compass_servant"
    private var mLooper: Handler? = null
    private var mUIHandler: Handler? = null
    private val MESSAGE = 0x110
    override fun getLayoutId(): Int {
        return R.layout.fragment_decibel_meter
    }

    override fun initView() {
        initHandlerThread()
        compass_servant?.setServantListener(this)
        compass_servant?.setPointerDecibel(118)
    }

    override fun initData() {

    }

    private fun initHandlerThread() {
        mUIHandler = Handler()
        mHandlerThread = HandlerThread(mHtName)
        mHandlerThread?.start()
        mLooper = object : Handler(mHandlerThread?.looper!!) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == MESSAGE && i > 0) {
                    doWithMainUI()
                    i--
                }
            }
        }
    }

    private var i = 1000
    private fun doWithMainUI() {
        try {
            mUIHandler?.post(Runnable {
                val d = Math.random() * 89
                val iRandom = d.toInt() + 30
                compass_servant!!.setPointerDecibel(iRandom)
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        mHandlerThread?.quit()
        super.onDestroy()
    }

    override fun startTension() {
        mLooper?.sendEmptyMessage(MESSAGE)
    }
}