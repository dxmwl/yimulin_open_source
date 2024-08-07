package com.yimulin.mobile.ui.fragment

import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.SelectMainStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.pdlbox.tools.utils.ClipboardUtils
import com.pdlbox.tools.utils.GlideUtils
import com.yimulin.mobile.utils.ImageFileCompressEngine
import com.shouzhong.scanner.ScannerUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.utils.GlideEngine
import com.yimulin.mobile.utils.MeSandboxFileEngine
import kotlin.concurrent.thread

/**
 * @ClassName: ScannerTextFragment
 * @Description: 图片文本识别
 * @Author: 常利兵
 * @Date: 2023/6/2 11:08
 **/
class ScannerTextFragment : com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>() {
    private lateinit var selectorStyle: PictureSelectorStyle

    private val show_img: ImageView? by lazy { findViewById(R.id.show_img) }
    private val tvResult: TextView? by lazy { findViewById(R.id.tv_result) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_scanner_text
    }

    override fun initView() {
        setOnClickListener(R.id.btn_choose_img,R.id.btn_copy)
        initSelectorUi()
    }

    override fun initData() {

    }

    @com.yimulin.mobile.aop.SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btn_choose_img -> {
                chooseUserAvatar()
            }
            R.id.btn_copy -> {
                val toString = tvResult?.text?.toString()
                if (toString.isNullOrBlank()) {
                    toast("内容为空")
                    return
                }
                ClipboardUtils.copyText(toString)
                toast("已复制到剪切板")
            }
            else -> {}
        }
    }

    /**
     * 选择用户头像
     */
    private fun chooseUserAvatar() {
        PictureSelector.create(this@ScannerTextFragment)
            .openGallery(SelectMimeType.ofImage())
            .setMaxSelectNum(1)
            .setSelectionMode(SelectModeConfig.SINGLE)
            .setImageEngine(GlideEngine.createGlideEngine())
            .setCompressEngine(ImageFileCompressEngine())
            .setSandboxFileEngine(MeSandboxFileEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    val media = result?.get(0)
                    val path = media?.availablePath
                    val imgLocalPath =
                        if (PictureMimeType.isContent(path) && !media?.isCut!! && !media.isCompressed) Uri.parse(
                            path
                        ) else path
                    GlideUtils.showImg(imgLocalPath.toString(), show_img)

                    thread {
                        val s = ScannerUtils.decodeText(imgLocalPath.toString())
                        runOnUiThread {
                            if (s.isNullOrBlank()){
                                toast("无法识别内容")
                                return@runOnUiThread
                            }
                            tvResult?.text = "$s"
                        }
                    }
                }

                override fun onCancel() {

                }
            })
    }

    /**
     * 初始化选择器UI
     */
    private fun initSelectorUi() {
        selectorStyle = PictureSelectorStyle()
        val whiteTitleBarStyle = TitleBarStyle()
        whiteTitleBarStyle.titleBackgroundColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_white)
        whiteTitleBarStyle.titleDrawableRightResource = R.drawable.ic_orange_arrow_down
        whiteTitleBarStyle.titleLeftBackResource = R.drawable.ps_ic_black_back
        whiteTitleBarStyle.titleTextColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_black)
        whiteTitleBarStyle.titleCancelTextColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_53575e)
        whiteTitleBarStyle.isDisplayTitleBarLine = true

        val whiteBottomNavBarStyle = BottomNavBarStyle()
        whiteBottomNavBarStyle.bottomNarBarBackgroundColor = Color.parseColor("#EEEEEE")
        whiteBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_53575e)

        whiteBottomNavBarStyle.bottomPreviewNormalTextColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_9b)
        whiteBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_fa632d)
        whiteBottomNavBarStyle.isCompleteCountTips = false
        whiteBottomNavBarStyle.bottomEditorTextColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_53575e)
        whiteBottomNavBarStyle.bottomOriginalTextColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_53575e)

        val selectMainStyle = SelectMainStyle()
        selectMainStyle.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_white)
        selectMainStyle.isDarkStatusBarBlack = true
        selectMainStyle.selectNormalTextColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_9b)
        selectMainStyle.selectTextColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_fa632d)
        selectMainStyle.previewSelectBackground = R.drawable.ps_checkbox_selector
        selectMainStyle.selectBackground = R.drawable.ps_checkbox_selector
        selectMainStyle.selectText = getString(R.string.ps_done_front_num)
        selectMainStyle.mainListBackgroundColor =
            ContextCompat.getColor(requireContext(), R.color.ps_color_white)

        selectorStyle.titleBarStyle = whiteTitleBarStyle
        selectorStyle.bottomBarStyle = whiteBottomNavBarStyle
        selectorStyle.selectMainStyle = selectMainStyle
    }
}