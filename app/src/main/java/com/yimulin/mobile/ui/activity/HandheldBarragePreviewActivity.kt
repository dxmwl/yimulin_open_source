package com.yimulin.mobile.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.hanks.htextview.rainbow.RainbowTextView
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.Log
import com.yimulin.mobile.app.AppActivity

/**
 * @ClassName: HandheldBarragePreviewActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/12/26 0026 11:09
 **/
class HandheldBarragePreviewActivity: AppActivity() {
    companion object {

        private val CONTENT_STR = "CONTENT_STR"
        private val FONT_STYLE = "FONT_STYLE"
        private val FONT_SIZE = "FONT_SIZE"
        private val SELECTED_TEXT_COLOR = "SELECTED_TEXT_COLOR"
        private val SELECTED_BACK_COLOR = "SELECTED_BACK_COLOR"

        @Log
        fun start(
            context: Context,
            contentStr: String,
            fontStyle: Int,
            fontSize: Int,
            selectedTextColor: Int,
            selectedBackColor: Int
        ) {
            val intent = Intent(context, HandheldBarragePreviewActivity::class.java)
            intent.putExtra(CONTENT_STR, contentStr)
            intent.putExtra(FONT_STYLE, fontStyle)
            intent.putExtra(FONT_SIZE, fontSize)
            intent.putExtra(SELECTED_TEXT_COLOR, selectedTextColor)
            intent.putExtra(SELECTED_BACK_COLOR, selectedBackColor)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val rainbowText: RainbowTextView? by lazy { findViewById(R.id.text_rainbow) }
    private val normalText: TextView? by lazy { findViewById(R.id.text_normal) }
    private val rootLayout: RelativeLayout? by lazy { findViewById(R.id.root_layout) }

    private var contentStr = "弹幕预览"
    private var fontStyle = 1
    private var fontSize = 100

    //设置对话框中选择的文本颜色、背景颜色、文本大小
    private var selectedTextColor = -0x10000
    private var selectedBackColor = -0x1

    override fun getLayoutId(): Int {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR)
        //在Window增加flag打开屏幕常亮：
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        return R.layout.activity_handheld_barrage_preview
    }

    override fun initView() {
        contentStr = getString(CONTENT_STR).toString()
        fontStyle = getInt(FONT_STYLE)
        fontSize = getInt(FONT_SIZE)
        selectedTextColor = getInt(SELECTED_TEXT_COLOR)
        selectedBackColor = getInt(SELECTED_BACK_COLOR)

        //初始化backColor、textColor、textSize
        setBackColor(selectedBackColor)
        setTextColor(selectedTextColor)
        setTextSize(fontSize)
        setTextContent(contentStr)
        checkIsSelectRainbow()
    }

    override fun initData() {
        
    }

    //设置字体颜色
    private fun setTextColor(color: Int) {
        normalText!!.setTextColor(color)
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
}