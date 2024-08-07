package com.yimulin.mobile.ui.fragment

import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.VibrateUtils
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.pdlbox.tools.utils.ClipboardUtils
import com.shouzhong.scanner.ScannerView
import com.yimulin.mobile.R
import com.yimulin.mobile.other.PermissionInterceptor
import com.yimulin.mobile.widget.ViewFinder

/**
 * @ClassName: ScannerTextFragment
 * @Description: 身份证识别
 * @Author: 常利兵
 * @Date: 2023/6/2 10:45
 **/
class ScannerFragment(
    //0:条码识别  1：二维码识别 2：银行卡识别 3：身份证识别 4：车牌识别 5：驾驶证识别
    val type: Int
) : com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>() {

    private val scannerView: ScannerView? by lazy { findViewById(R.id.sv) }
    private val tvResult: TextView? by lazy { findViewById(R.id.tv_result) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_scanner_id_card
    }

    override fun initView() {

        setOnClickListener(R.id.btn_copy)

        XXPermissions.with(this)
            .permission(Permission.CAMERA)
            .interceptor(PermissionInterceptor())
            .request { _, all ->
                if (all) {
                    startScanner()
                }
            }
    }

    private fun startScanner() {
        scannerView?.setShouldAdjustFocusArea(true)
        scannerView?.setViewFinder(
            ViewFinder(
                requireContext()
            )
        )
        scannerView?.setSaveBmp(false)
        scannerView?.setRotateDegree90Recognition(true)
        scannerView?.setCallback { result ->
            if (result == null) {
                toast("无法识别内容")
                return@setCallback
            }
            tvResult?.text = "$result"
            VibrateUtils.vibrate(300)
            scannerView?.restartPreviewAfterDelay(2000)
        }
        scannerView?.onResume()
        when (type) {
            0 -> {
                //条码识别
                scannerView?.setEnableZBar(true)
                scannerView?.setEnableBarcode(true)
            }
            1 -> {
                //条码识别
                scannerView?.setEnableZXing(true)
                scannerView?.setEnableQrcode(true)
            }
            2 -> {
                //银行卡识别
                scannerView?.setEnableBankCard(true)
            }
            3 -> {
                //身份证识别
                scannerView?.setEnableIdCard2(true)
            }
            4 -> {
                //车牌识别
                scannerView?.setEnableLicensePlate(true)
            }
            5 -> {
                //驾驶证识别
                scannerView?.setEnableDrivingLicense(true)
            }
            else -> {}
        }
    }

    override fun initData() {

    }

    @com.yimulin.mobile.aop.SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
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

    override fun onResume() {
        super.onResume()
        scannerView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        scannerView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        VibrateUtils.cancel()
    }
}