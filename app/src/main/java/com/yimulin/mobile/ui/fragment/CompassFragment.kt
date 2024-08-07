package com.yimulin.mobile.ui.fragment

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.widget.ChaosCompassView

/**
 * @ClassName: CompassFragment
 * @Description:指南针
 * @Author: 常利兵
 * @Date: 2023/5/16 16:18
 **/
class CompassFragment: AppFragment<AppActivity>() {

    private var mSensorEventListener: SensorEventListener? = null
    private var sensorManager: SensorManager? = null
    private val ccv: ChaosCompassView? by lazy { findViewById(R.id.ccv) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_compass
    }

    override fun initView() {
        sensorManager = ContextCompat.getSystemService(requireContext(), SensorManager::class.java)
        val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        mSensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val value = event.values[0]
                ccv?.setVal(value)
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }


        sensorManager?.registerListener(mSensorEventListener,sensor,SensorManager.SENSOR_DELAY_GAME)
    }

    override fun initData() {
        
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(mSensorEventListener)
    }
}