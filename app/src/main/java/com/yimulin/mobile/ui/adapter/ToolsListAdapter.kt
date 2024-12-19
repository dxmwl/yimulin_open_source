package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
//import com.bytedance.sdk.openadsdk.AdSlot
//import com.bytedance.sdk.openadsdk.TTAdConstant
//import com.bytedance.sdk.openadsdk.TTAdNative
//import com.bytedance.sdk.openadsdk.TTAdSdk
//import com.bytedance.sdk.openadsdk.TTRewardVideoAd
import com.hjq.base.BaseDialog
import com.hjq.shape.view.ShapeView
import com.hjq.toast.ToastUtils
import com.kwad.sdk.core.b.a.it
import com.orhanobut.logger.Logger
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppAdapter
import com.yimulin.mobile.http.model.ToolsDto
import com.yimulin.mobile.ui.activity.ToolsDetailActivity
import com.yimulin.mobile.ui.dialog.MessageDialog


/**
 * @ClassName: ToolsListAdapter
 * @Description:工具适配器
 * @Author: 常利兵
 * @Date: 2023/5/12 15:15
 **/
class ToolsListAdapter(val mContext: Context) :
    AppAdapter<ToolsDto>(mContext) {

//    //@[classname]
//    private var rewardVideoAd: TTRewardVideoAd? = null
//    //@[classname]
//    private var adNativeLoader: TTAdNative? = null
//    //@[classname]
//    private var rewardVideoAdListener: TTAdNative.RewardVideoAdListener? = null
//    //@[classname]
//    private var rewardAdInteractionListener: TTRewardVideoAd.RewardAdInteractionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_tools) {

        private val tools_name: TextView? by lazy { findViewById(R.id.tools_name) }
        private val tools_type: ShapeView? by lazy { findViewById(R.id.tools_type) }

        override fun onBindView(position: Int) {
            val toolsDto = getItem(position)
            tools_name?.text = toolsDto.toolsName
            tools_type?.visibility = View.VISIBLE
            when (toolsDto.serviceType) {
                0 -> {
                    tools_type?.visibility = View.INVISIBLE
                }

                1 -> {
                    tools_type?.shapeDrawableBuilder
                        ?.setSolidColor(Color.parseColor("#FFFF0000"))
                        ?.intoBackground()
                }

                2 -> {
                    tools_type?.shapeDrawableBuilder
                        ?.setSolidColor(Color.parseColor("#FF0000FF"))
                        ?.intoBackground()
                }

                else -> {}
            }

            getItemView().setOnClickListener {
                if (toolsDto.isNeedViewAd) {
                    MessageDialog.Builder(mContext)
                        .setTitle("温馨提示")
                        .setMessage("该功能仅需观看一次广告即可免费解锁，是否观看广告")
                        .setCancel("以后再说")
                        .setConfirm("观看广告")
                        .setListener(object : MessageDialog.OnListener {
                            override fun onConfirm(dialog: BaseDialog?) {
//                                loadAd(toolsDto)
                            }
                        })
                        .show()
                } else {
                    jumpToolsDetail(toolsDto)
                }
            }
        }
    }

    /**
     * 跳转工具详情
     */
    private fun jumpToolsDetail(toolsDto: ToolsDto) {
        ToolsDetailActivity.start(
            mContext,
            url = toolsDto.url,
            toolsId = toolsDto.toolsId,
            toolsTitle = toolsDto.toolsName,
            isFullScreen = toolsDto.isFullScreen,
            isNeedSearch = toolsDto.isNeedSearch,
            isNeedViewAd = toolsDto.isNeedViewAd
        )
    }
//
//    private fun loadAd(toolsDto: ToolsDto) {
//
//        /** 1、创建AdSlot对象 */
//        //@[classname]
//        val adslot = AdSlot.Builder()
//            .setCodeId("102635486")
//            //@[classname]
//            .setOrientation(TTAdConstant.VERTICAL)
//            .build()
//
//        /** 2、创建TTAdNative对象 */
//        //@[classname]//@[methodname]
//        adNativeLoader = TTAdSdk.getAdManager().createAdNative(ActivityUtils.getTopActivity())
//
//        /** 3、创建加载、展示监听器 */
//        initListeners(toolsDto)
//
//        /** 4、加载广告 */
//        adNativeLoader?.loadRewardVideoAd(adslot, rewardVideoAdListener)
//    }
//
//    private fun showAd() {
//        if (rewardVideoAd == null) {
//            Logger.d("请先加载广告或等待广告加载完毕后再调用show方法")
//        }
//        rewardVideoAd?.let {
//            if (it.mediationManager.isReady) {
//                /** 5、设置展示监听器，展示广告 */
//                it.setRewardAdInteractionListener(rewardAdInteractionListener)
//                it.showRewardVideoAd(ActivityUtils.getTopActivity())
//            }
//        }
//    }
//
//    private fun initListeners(toolsDto: ToolsDto) {
//        // 广告加载监听器
//        //@[classname]
//        rewardVideoAdListener = object : TTAdNative.RewardVideoAdListener {
//            override fun onError(code: Int, message: String?) {
//                Logger.d("onError code = ${code} msg = ${message}")
//                jumpToolsDetail(toolsDto)
//            }
//
//            //@[classname]
//            override fun onRewardVideoAdLoad(ad: TTRewardVideoAd?) {
//                Logger.d("onRewardVideoAdLoad")
//                rewardVideoAd = ad
//            }
//
//            override fun onRewardVideoCached() {
//                Logger.d("onRewardVideoCached")
//            }
//
//            //@[classname]
//            override fun onRewardVideoCached(ad: TTRewardVideoAd?) {
//                Logger.d("onRewardVideoCached")
//                rewardVideoAd = ad
//                showAd()
//            }
//        }
//        var getReward = false
//
//        // 广告展示监听器
//        //@[classname]
//        rewardAdInteractionListener = object : TTRewardVideoAd.RewardAdInteractionListener {
//            override fun onAdShow() {
//                Logger.d("onAdShow")
//            }
//
//            override fun onAdVideoBarClick() {
//                Logger.d("onAdVideoBarClick")
//            }
//
//            override fun onAdClose() {
//                Logger.d("onAdClose")
//                if (getReward) {
//                    jumpToolsDetail(toolsDto)
//                } else {
//                    ToastUtils.show("广告未播放完成，功能解锁失败")
//                }
//            }
//
//            override fun onVideoComplete() {
//                Logger.d("onVideoComplete")
//            }
//
//            override fun onVideoError() {
//                Logger.d("onVideoError")
//                jumpToolsDetail(toolsDto)
//            }
//
//            override fun onRewardVerify(
//                rewardVerify: Boolean,
//                rewardAmount: Int,
//                rewardName: String?,
//                errorCode: Int,
//                errorMsg: String?
//            ) {
//                //此方法不生效
//            }
//
//            override fun onRewardArrived(
//                isRewardValid: Boolean,
//                rewardType: Int,
//                extraInfo: Bundle?
//            ) {
//                getReward = true
//                Logger.d("onRewardArrived, extra: " + extraInfo?.toString())
//            }
//
//            override fun onSkippedVideo() {
//                Logger.d("onSkippedVideo")
//            }
//        }
//    }
}