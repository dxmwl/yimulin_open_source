package com.yimulin.mobile.ui.fragment

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.RingtoneManager
import android.net.Uri
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.ihat.pihat.circleprogress.CircleProgress
import com.yimulin.mobile.R
import java.math.BigDecimal
import kotlin.math.sqrt


/**
 * @ClassName: MetalDetectorFragment
 * @Description: 金属探测仪
 * @Author: 常利兵
 * @Date: 2023/5/13 12:43
 **/
class MetalDetectorFragment : com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>(), SensorEventListener {
    private var sensor: Sensor? = null
    private var sensorManager: SensorManager? = null
    private val total_value: TextView? by lazy { findViewById(R.id.total_value) }
    private val status_tv: TextView? by lazy { findViewById(R.id.status_tv) }
    private val textViewX: TextView? by lazy { findViewById(R.id.text_view_x) }
    private val textViewY: TextView? by lazy { findViewById(R.id.text_view_y) }
    private val textViewZ: TextView? by lazy { findViewById(R.id.text_view_z) }
    private val progressBarX: ProgressBar? by lazy { findViewById(R.id.progress_bar_x) }
    private val progressBarY: ProgressBar? by lazy { findViewById(R.id.progress_bar_y) }
    private val progressBarZ: ProgressBar? by lazy { findViewById(R.id.progress_bar_z) }
    private val circleProgressX: CircleProgress? by lazy { findViewById(R.id.circle_progress_x) }
    private val circleProgressY: CircleProgress? by lazy { findViewById(R.id.circle_progress_y) }
    private val circleProgressZ: CircleProgress? by lazy { findViewById(R.id.circle_progress_z) }
    private val lineChart: LineChart? by lazy { findViewById(R.id.line_chart) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_metal_detector
    }

    override fun initView() {
        lineChart?.let { initChart(it) }
        initSensor()
    }

    override fun initData() {

    }

    private fun initSensor() {
        sensorManager = getSystemService(requireContext(), SensorManager::class.java)
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        // Register SensorManager listener.
        sensorManager?.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }


    override fun onPause() {
        super.onPause()
        // Unregister SensorManager listener when callback onPause().
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        val rawTotal: Double //未处理的数据
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            //分别计算三轴磁感应强度
            val X_lateral: Float = event.values[0]
            val Y_lateral: Float = event.values[1]
            val Z_lateral: Float = event.values[2]

            textViewX!!.text = X_lateral.toString()
            progressBarX!!.progress = 0 - X_lateral.toInt()
            circleProgressX!!.setValueText(X_lateral.toString())
            circleProgressX!!.setSweepValue(-X_lateral)

            textViewY!!.text = Y_lateral.toString()
            progressBarY!!.progress = Y_lateral.toInt()
            circleProgressY!!.setValueText(Y_lateral.toString())
            circleProgressY!!.setSweepValue(Y_lateral)

            textViewZ!!.text = Z_lateral.toString()
            progressBarZ!!.progress = 0 - Z_lateral.toInt()
            circleProgressZ!!.setValueText(Z_lateral.toString())
            circleProgressZ!!.setSweepValue(Z_lateral)

            //计算出总磁感应强度
            rawTotal =
                sqrt((X_lateral * X_lateral + Y_lateral * Y_lateral + Z_lateral * Z_lateral).toDouble())
            //初始化BigDecimal类
            val total = BigDecimal(rawTotal)
            val res = total.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
            total_value?.text = "总磁感应强度${res} μT"

            if (res < 80) {
                status_tv?.setTextColor(Color.rgb(0, 0, 0)) //设置文字颜色为黑色
                status_tv?.text = "未探测到金属"
            } else {
                status_tv?.setTextColor(Color.rgb(255, 0, 0)) //红色
                status_tv?.text = "探测到金属!"
                //震动
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val rt = RingtoneManager.getRingtone(requireContext(), uri)
                rt.play()
            }

            //绘制曲线
            addData(res.toFloat())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private var xAxis //X轴
            : XAxis? = null
    private var leftYAxis //左侧Y轴
            : YAxis? = null
    private var rightYaxis //右侧Y轴
            : YAxis? = null
    private var legend //图例
            : Legend? = null

    /**
     * 初始化图表
     */
    private fun initChart(lineChart: LineChart) {
        /***图表设置 */
        //是否展示网格线
        lineChart.setDrawGridBackground(false)
        //是否显示边界
        lineChart.setDrawBorders(false)
        //是否可以拖动
        lineChart.isDragEnabled = true
        //是否有触摸事件
        lineChart.setTouchEnabled(true)
        //设置XY轴动画效果
        lineChart.animateY(2500)
        lineChart.animateX(1500)
        /***XY轴的设置 */
        xAxis = lineChart.xAxis
        leftYAxis = lineChart.axisLeft
        rightYaxis = lineChart.axisRight
        //X轴设置显示位置在底部
        xAxis?.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis?.setAxisMinimum(0f)
        xAxis?.setGranularity(1f)
        //保证Y轴从0开始，不然会上移一点
        leftYAxis?.setAxisMinimum(0f)
        rightYaxis?.setAxisMinimum(0f)
        /***折线图例 标签 设置 */
        legend = lineChart.legend
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend?.setForm(Legend.LegendForm.LINE)
        legend?.setTextSize(12f)
        //显示位置 左下方
        legend?.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        legend?.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        legend?.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        //是否绘制在图表里面
        legend?.setDrawInside(false)

        showLineChart("磁感强度",R.color.colorPrimary)

    }
    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private fun initLineDataSet(lineDataSet: LineDataSet, color: Int, mode: LineDataSet.Mode?) {
        lineDataSet.color = color
        lineDataSet.setCircleColor(color)
        lineDataSet.lineWidth = 1f
        lineDataSet.circleRadius = 1f
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.valueTextSize = 0f
        //设置折线图填充
        lineDataSet.setDrawFilled(true)
        lineDataSet.formLineWidth = 1f
        lineDataSet.formSize = 15f
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        } else {
            lineDataSet.mode = mode
        }
    }

    private var lineData = ArrayList<Entry>()
    private lateinit var lineDataSet:LineDataSet
    /**
     * 展示曲线
     *
     * @param name     曲线名称
     * @param color    曲线颜色
     */
    private fun showLineChart(name: String?, color: Int) {
        // 每一个LineDataSet代表一条线
        lineDataSet = LineDataSet(lineData, name)
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR)
        val lineData = LineData(lineDataSet)
        lineChart?.data = lineData
    }

    private var position = 0

    private fun addData(res: Float) {
        lineDataSet = lineChart?.getData()?.getDataSetByIndex(0) as LineDataSet
        val ciganDto = Entry(position * 5f, res)
        val values = lineDataSet.values
        values.add(ciganDto)
        lineDataSet.setValues(values)

        lineChart?.data?.notifyDataChanged()
        lineChart?.notifyDataSetChanged()
        // 将图表滚动到最新的点处
        lineChart?.moveViewToX(lineDataSet.entryCount -4f)
        position++
    }
}