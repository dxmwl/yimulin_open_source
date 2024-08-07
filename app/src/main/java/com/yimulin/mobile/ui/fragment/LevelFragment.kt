package com.yimulin.mobile.ui.fragment

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat
import com.yimulin.mobile.R
import com.yimulin.mobile.widget.SpiritView

/**
 * @ClassName: LevelFragment
 * @Description: 水平仪
 * @Author: 常利兵
 * @Date: 2023/5/16 17:51
 **/
class LevelFragment : com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private val show: SpiritView? by lazy { findViewById(R.id.show) }

    //定义水平仪能处理的最大倾斜角度，超过该角度气泡直接位于边界
    private val MAX_ANGLE = 30

    override fun getLayoutId(): Int {
        return R.layout.fragment_level
    }

    override fun initView() {
        sensorManager = ContextCompat.getSystemService(requireContext(), SensorManager::class.java)
        val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        //注册
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun initData() {

    }

    private fun isContain(x: Int, y: Int): Boolean {
        //计算气泡的圆心坐标X，y
        val bubbleCx = x + show!!.bubble.width / 2
        val bubbleCy = y + show!!.bubble.width / 2
        //计算水平仪仪表盘圆心的坐标
        val backCx = show!!.back.width / 2
        val backCy = show!!.back.width / 2
        //计算气泡的圆心与水平仪表盘的圆心之间的距离
        val distance = Math.sqrt(
            ((bubbleCx - backCx) * (bubbleCx * backCx) +
                    (bubbleCy - backCy) * (bubbleCy - backCy)).toDouble()
        )
        //若两圆心的距离小于他们的半径差，即可认为处于该点的气泡任然位于仪表盘内
        return if (distance < show!!.back.width - show!!.bubble.width) {
            true
        } else {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //取消注册
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (sensorEvent == null) return
        val values: FloatArray = sensorEvent.values
        //获取传感器的类型
        //获取传感器的类型
        val sensorType: Int = sensorEvent.sensor.getType()
        when (sensorType) {
            Sensor.TYPE_ORIENTATION -> {
                //获取与Y轴的夹角
                val yAngle = values[1]
                //获取与Z轴的夹角
                val zAngle = values[2]
                //气泡位于中间时（水平仪完全水平）
                var x = (show!!.back.width - show!!.bubble.width) / 2
                var y = (show!!.back.height - show!!.bubble.height) / 2
                //如果与Z轴的倾斜角还在最大角度之内
                if (Math.abs(zAngle) <= MAX_ANGLE) {
                    //根据与Z轴的倾斜角度计算X坐标轴的变化值
                    val deltaX = ((show!!.back.width - show!!.bubble.width) / 2
                            * zAngle / MAX_ANGLE).toInt()
                    x += deltaX
                } else if (zAngle > MAX_ANGLE) {
                    x = 0
                } else {
                    x = show!!.back.width - show!!.bubble.width
                }

                //如果与Y轴的倾斜角还在最大角度之内
                if (Math.abs(yAngle) <= MAX_ANGLE) {
                    //根据与Z轴的倾斜角度计算X坐标轴的变化值
                    val deltaY = ((show!!.back.height - show!!.bubble.height) / 2
                            * yAngle / MAX_ANGLE).toInt()
                    y += deltaY
                } else if (yAngle > MAX_ANGLE) {
                    y = show!!.back.height - show!!.bubble.height
                } else {
                    y = 0
                }
                //如果计算出来的X，Y坐标还位于水平仪的仪表盘之内，则更新水平仪气泡坐标
                if (true) {
                    show!!.bubbleX = x
                    show!!.bubbleY = y
                    //Toast.makeText(Spirit.this, "在仪表盘内", Toast.LENGTH_SHORT).show();
                }
                //通知组件更新
                show!!.postInvalidate()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}