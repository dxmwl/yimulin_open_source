package com.yimulin.mobile.ui.fragment

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.hanks.htextview.rainbow.RainbowTextView
import com.hjq.shape.view.ShapeEditText
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.ui.activity.HandheldBarragePreviewActivity

/**
 * @ClassName: HandHeldBarrageFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/12/26 0026 10:38
 **/
class HandHeldBarrageFragment : AppFragment<AppActivity>() {

    private val input_content: ShapeEditText? by lazy { findViewById(R.id.input_content) }
    private val radio_group: RadioGroup? by lazy { findViewById(R.id.radio_group) }
    private val rainbowText: RainbowTextView? by lazy { findViewById(R.id.text_rainbow) }
    private val normalText: TextView? by lazy { findViewById(R.id.text_normal) }
    private val tvFontSize: TextView? by lazy { findViewById(R.id.tv_font_size) }
    private val seekBar: SeekBar? by lazy { findViewById(R.id.seek_bar) }
    private val btBackColor: Button? by lazy { findViewById(R.id.bt_back_color) }
    private val btTextColor: Button? by lazy { findViewById(R.id.bt_text_color) }
    private val rootLayout: RelativeLayout? by lazy { findViewById(R.id.root_layout) }
    private var contentStr = "弹幕预览"
    private var fontStyle = 1
    private var fontSize = 100

    //设置对话框中选择的文本颜色、背景颜色、文本大小
    private var selectedTextColor = -0x10000
    private var selectedBackColor = -0x1
    override fun getLayoutId(): Int {
        return R.layout.fragment_hand_held_barrage
    }

    override fun initView() {

        setOnClickListener(R.id.preview_in_full_screen)

        input_content?.addTextChangedListener {
            contentStr = it.toString()
            setTextContent(contentStr)
        }
        radio_group?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_rainbow -> {
                    fontStyle = 1
                    showRainbowText()
                }

                R.id.radio_normal -> {
                    fontStyle = 0
                    showNormalText()
                }

                else -> {}
            }
        }

        //选择字体大小
        seekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                fontSize = progress
                tvFontSize?.text = fontSize.toString()
                setTextSize(fontSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        //选择背景色
        btBackColor!!.setOnClickListener { showColorPicker(selectedBackColor, true) }
        //选择字体颜色
        btTextColor!!.setOnClickListener { showColorPicker(selectedTextColor, false) }

        //初始化backColor、textColor、textSize

        setBackColor(selectedBackColor)
        setTextColor(selectedTextColor)
        setTextSize(fontSize)
        setTextContent(contentStr)
        updateSelectedColor()
        checkIsSelectRainbow()
    }

    override fun initData() {

    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.preview_in_full_screen -> {
                HandheldBarragePreviewActivity.start(requireContext(), contentStr, fontStyle, fontSize, selectedTextColor, selectedBackColor)
            }
            else -> {}
        }
    }

    /**
     * 对选择的是彩虹字体还是普通字体进行处理
     * 1 展示的字体发生变化   2 设置对话框发生变化
     */
    private fun checkIsSelectRainbow() {
        if (fontStyle==1) {
            showRainbowText()
        } else {
            showNormalText()
        }
    }


    //设置背景颜色
    private fun setBackColor(color: Int) {
        rootLayout?.setBackgroundColor(color)
    }

    //设置显示内容
    private fun setTextContent(content: String) {
        rainbowText!!.text = content
        normalText!!.text = content
    }

    //设置字体颜色
    private fun setTextColor(color: Int) {
        normalText!!.setTextColor(color)
    }

    //显示彩虹字体
    private fun showRainbowText() {
        rainbowText?.visibility = View.VISIBLE
        normalText?.visibility = View.GONE
        rainbowText?.requestFocus()
    }

    //显示正常字体
    private fun showNormalText() {
        rainbowText?.visibility = View.GONE
        normalText?.visibility = View.VISIBLE
        normalText?.requestFocus()
    }

    //设置字体大小
    private fun setTextSize(size: Int) {
        rainbowText?.textSize = size.toFloat()
        normalText?.textSize = size.toFloat()
    }

    /**
     * 颜色选择器弹窗
     * @param initColor  初始化颜色
     * @param isChooseBackColor 是否为选择背景色
     */
    private fun showColorPicker(initColor: Int, isChooseBackColor: Boolean) {
        ColorPickerDialogBuilder
            .with(requireContext())
            .initialColor(initColor)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .showBorder(false)
            .setPositiveButton("选择"
            ) { dialog, selectedColor, allColors ->
                if (isChooseBackColor) {
                    selectedBackColor = selectedColor
                } else {
                    selectedTextColor = selectedColor
                }
                setBackColor(selectedBackColor)
                setTextColor(selectedTextColor)
                updateSelectedColor()
            }
            .setNegativeButton("取消",
                { dialog, which -> })
            .build()
            .show()
    }

    //更新选择的颜色
    private fun updateSelectedColor() {
        (btBackColor!!.background as GradientDrawable).setColor(selectedBackColor)
        (btTextColor!!.background as GradientDrawable).setColor(selectedTextColor)
    }
}