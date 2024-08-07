package com.yimulin.mobile.ui.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ThreadUtils
import com.hjq.permissions.Permission.RECORD_AUDIO
import com.hjq.permissions.XXPermissions
import com.maple.msdialog.AlertDialog
import com.yimulin.mobile.R
import com.yimulin.mobile.other.PermissionInterceptor
import com.yimulin.mobile.utils.ArrayUtils
import com.yimulin.mobile.utils.MediaRecorderDemo
import com.yimulin.mobile.widget.BrokenLineView
import java.util.ArrayList

/**
 * @ClassName: NoiseMeasurementFragment
 * @Description: 噪音测量
 * @Author: 常利兵
 * @Date: 2023/5/16 18:38
 **/
class NoiseMeasurementFragment : com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>() {

    companion object {
        /** 检测时间最大时间  */
        const val checkTime = 15000 * 1000

        /** 更新噪音标志  */
        const val UPDATE_NOISE_VALUE = 1
    }

    /** 检测噪音的开始时间  */
    private var startTime: Long = 0

    /** 检测噪音工具类  */
    private var media: MediaRecorderDemo? = null
    private lateinit var mBrokenLine: BrokenLineView

    private var maxVolume = 0.0
    private var minVolume = 99990.0

    /** 检测到的所有噪音分贝值  */
    private val allVolume = ArrayList<Double>()

    /** 噪音分贝值 的说明文字  */
    private lateinit var dbExplain: Array<String>

    private val tv_noise_value: TextView? by lazy { findViewById(R.id.tv_noise_value) }
    private val ll_chart_view: LinearLayout? by lazy { findViewById(R.id.ll_chart_view) }
    private val tv_max_value: TextView? by lazy { findViewById(R.id.tv_max_value) }
    private val tv_db_explain1: TextView? by lazy { findViewById(R.id.tv_db_explain1) }
    private val tv_db_explain2: TextView? by lazy { findViewById(R.id.tv_db_explain2) }
    private val tv_min_value: TextView? by lazy { findViewById(R.id.tv_min_value) }
    private val tv_avg_value: TextView? by lazy { findViewById(R.id.tv_avg_value) }

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UPDATE_NOISE_VALUE// 更新噪音值
                -> {
                    val db = msg.obj as Double
                    val time = System.currentTimeMillis() - startTime
                    if (time >= checkTime) {// 检测完成
                        media!!.stopRecord()
//                        showDialog()
                        ThreadUtils.runOnUiThread {
                            toast("检测结束")
                        }
                    }
                    mBrokenLine.updateDate(ArrayUtils.sub(allVolume, mBrokenLine.maxCacheNum))
                    updateNoise(db)
                }
                2// 进入下一个界面
                -> {
                }
            }
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_noise_measurement
    }

    override fun initView() {
        mBrokenLine = BrokenLineView(requireContext())
        ll_chart_view?.addView(
            mBrokenLine.execute(), FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
            )
        )

        dbExplain = resources.getStringArray(R.array.db_explain_arr)
        XXPermissions.with(this)
            .permission(RECORD_AUDIO)
            .interceptor(PermissionInterceptor("申请录音权限，用于检测噪音大小"))
            .request { permissions, all ->
                if (all) {
                    handler.post(checkNoise)
                }
            }
    }

    override fun initData() {

    }

    /**
     * 检测噪音
     */
    private var checkNoise: Runnable = Runnable {
        // 波动较大。用的较多
        media = MediaRecorderDemo { noiseValue ->
            val msg = Message.obtain()
            msg.what = UPDATE_NOISE_VALUE
            msg.obj = noiseValue
            handler.sendMessage(msg)
        }
        media!!.startRecord()
        startTime = System.currentTimeMillis()
    }

    override fun onDestroy() {
        media?.stopRecord()// 停止检测
        super.onDestroy()
    }

    /**
     * 更新噪音分贝值
     *
     * @param db
     */
    @SuppressLint("SetTextI18n")
    private fun updateNoise(db: Double) {
        // Log.e("", "noiseValue：" + db);
        // 更新当前值
        tv_noise_value?.text = db.toInt().toString() + " dB"
        // 更新最大值
        if (db > maxVolume) {
            maxVolume = db
            tv_max_value?.text = "最高分贝:\n " + maxVolume.toInt() + " dB"
        }
        // 更新最小值
        if (db < minVolume && db != 0.0) {
            minVolume = db
            tv_min_value?.text = "最低分贝:\n " + minVolume.toInt() + " dB"
        }
        // 更新平均值
        if (db != 0.0) {
            allVolume.add(db)
            val avgVolume = ArrayUtils.avg(allVolume)
            tv_db_explain1?.text = dbExplain[(avgVolume / 10).toInt()]
            tv_db_explain2?.text = dbExplain[(avgVolume / 10).toInt() + 1]
            tv_avg_value?.text = "平均分贝:\n " + avgVolume.toInt() + " dB"
        }
    }

    override fun showDialog() {
        ActivityUtils.getTopActivity().runOnUiThread {

            // 平均噪音分贝 > 40dB
            if (ArrayUtils.avg(allVolume) > 40) {
                AlertDialog(ActivityUtils.getTopActivity())
                    .setScaleWidth(0.7)
                    .setMessage("您的监测环境不适合后面的测试，请您到较安静的环境下测试。")
                    .setLeftButton("取消", null)
                    .setRightButton("重新检测") { activity?.onBackPressed() }
                    .show()
            } else {
                AlertDialog(ActivityUtils.getTopActivity())
                    .setScaleWidth(0.7)
                    .setMessage("您的测试环境良好，可以继续后面测试。")
                    .setLeftButton("取消", null)
                    .setRightButton("进入测试") { toCheckEar() }
                    .show()
            }
        }
    }

    /**
     * 去检查耳朵
     */
    private fun toCheckEar() {
        activity?.onBackPressed()
//        val intent = Intent(requireContext(), DetectionActivity::class.java)
//        startActivity(intent)
    }
}