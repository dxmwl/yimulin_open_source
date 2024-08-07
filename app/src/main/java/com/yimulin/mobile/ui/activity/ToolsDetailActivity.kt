package com.yimulin.mobile.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.interfaces.listener.IDJXDramaHomeListener
import com.bytedance.sdk.djx.model.DJXDrama
import com.bytedance.sdk.djx.model.DJXDramaDetailConfig
import com.bytedance.sdk.djx.model.DJXDramaUnlockAdMode
import com.bytedance.sdk.djx.params.DJXWidgetDramaHomeParams
import com.bytedance.sdk.dp.DPSdk
import com.bytedance.sdk.dp.DPWidgetGridParams
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdDislike
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.hjq.bar.TitleBar
import com.hjq.base.BaseDialog
import com.hjq.base.livebus.LiveDataBus
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.umeng.Platform
import com.hjq.umeng.UmengClient
import com.hjq.umeng.UmengShare
import com.hjq.widget.view.ClearEditText
import com.orhanobut.logger.Logger
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.ui.fragment.exchange.ExchangeFragment
import com.yimulin.mobile.ui.fragment.general.GeneralFragment
import com.yimulin.mobile.ui.fragment.relation.RelationFragment
import com.yimulin.mobile.ui.fragment.Base64EncryptFragment
import com.yimulin.mobile.ui.fragment.Md5EncryptFragment
import com.yimulin.mobile.ui.fragment.MorseCodeFragment
import com.yimulin.mobile.ui.fragment.RsaEncryptFragment
import com.yimulin.mobile.ui.fragment.ScannerFragment
import com.yimulin.mobile.ui.fragment.ScannerTextFragment
import com.yimulin.mobile.ui.fragment.LocationInfoFragment
import com.yimulin.mobile.ui.fragment.RouteLookupFragment
import com.yimulin.mobile.R
import com.yimulin.mobile.other.PermissionInterceptor
import com.yimulin.mobile.ui.dialog.MessageDialog
import com.yimulin.mobile.ui.dialog.ShareDialog
import com.yimulin.mobile.ui.fragment.*
import com.yimulin.mobile.ui.fragment.ZipCodeInquiryFragment
import com.yimulin.mobile.utils.djUtils.DJXHolder


/**
 * @ClassName: ToolsDetailActivity
 * @Description: 工具详情
 * @Author: 常利兵
 * @Date: 2023/5/12 21:10
 **/
class ToolsDetailActivity : AppActivity() {

    companion object {
        private const val TOOLS_ID = "TOOLS_ID"
        private const val TOOLS_TITLE = "TOOLS_TITLE"
        private const val IS_FULL_SCREEN = "IS_FULL_SCREEN"
        private const val IS_NEED_SEARCH = "IS_NEED_SEARCH"
        private const val IS_NEED_VIEW_AD = "IS_NEED_VIEW_AD"
        private const val URL = "URL"

        fun start(
            context: Context,
            toolsId: Int,
            toolsTitle: String = "",
            url: String? = "",
            isFullScreen: Boolean = false,
            isNeedSearch: Boolean = false,
            isNeedViewAd: Boolean = false
        ) {
            val intent = Intent(context, ToolsDetailActivity::class.java)
            intent.putExtra(TOOLS_ID, toolsId)
            intent.putExtra(TOOLS_TITLE, toolsTitle)
            intent.putExtra(IS_FULL_SCREEN, isFullScreen)
            intent.putExtra(IS_NEED_SEARCH, isNeedSearch)
            intent.putExtra(IS_NEED_VIEW_AD, isNeedViewAd)
            intent.putExtra(URL, url)
            context.startActivity(intent)
        }
    }

    private val title_bar: TitleBar? by lazy { findViewById(R.id.title_bar) }
    private val layout_search: RelativeLayout? by lazy { findViewById(R.id.layout_search) }
    private val input_keyword: ClearEditText? by lazy { findViewById(R.id.input_keyword) }
    private val bannerContainer: FrameLayout? by lazy { findViewById(R.id.ad_view) }

    //@[classname]
    private var bannerAd: TTNativeExpressAd? = null

    //@[classname]
    private var adNativeLoader: TTAdNative? = null

    //@[classname]
    private var adSlot: AdSlot? = null

    //@[classname]
    private var nativeExpressAdListener: TTAdNative.NativeExpressAdListener? = null

    //@[classname]
    private var expressAdInteractionListener: TTNativeExpressAd.ExpressAdInteractionListener? = null

    //@[classname]
    private var dislikeInteractionCallback: TTAdDislike.DislikeInteractionCallback? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_tools_detail
    }

    override fun initView() {

        setOnClickListener(R.id.btn_search)

        title = getString(TOOLS_TITLE)

        val isNeedSearch = getBoolean(IS_NEED_SEARCH)
        val isFullScreen = getBoolean(IS_FULL_SCREEN)
        if (isFullScreen) {
            title_bar?.visibility = View.GONE
        } else {
            if (isNeedSearch) {
                title_bar?.visibility = View.VISIBLE
                layout_search?.visibility = View.VISIBLE
            } else {
                title_bar?.visibility = View.VISIBLE
                layout_search?.visibility = View.INVISIBLE
            }
        }

        switchFragment()

        input_keyword?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                val keyWork = input_keyword?.text?.toString()
                if (keyWork == null || keyWork.isBlank()) {
                    toast("请输入要搜索的内容")
                    true
                }
                if (keyWork != null) {
                    LiveDataBus.postValue("search", keyWork)
                }
                hideKeyboard(v)
            }
            true
        }
    }

    override fun initData() {
        val isFullScreen = getBoolean(IS_FULL_SCREEN)
        if (!isFullScreen) {
            loadAd()
        }
    }

    override fun onLeftClick(view: View) {
        super.onLeftClick(view)
    }

    override fun onRightClick(view: View) {
        val permissionList = ArrayList<String>()
        if (Build.VERSION.SDK_INT < 33) {
            permissionList.add(Permission.READ_MEDIA_IMAGES)
        } else {
            showShareDialog()
            return
        }
        XXPermissions.with(this)
            .permission(permissionList)
            .interceptor(PermissionInterceptor("申请存储读写权限，用于分享功能"))
            .request { permissions, all ->
                if (all) {
                    showShareDialog()
                }
            }
    }

    private fun showShareDialog() {
        val umImage = UMImage(
            this, ConvertUtils.drawable2Bytes(
                ContextCompat.getDrawable(this, R.mipmap.ic_launcher)
            )
        )
        val umWeb =
            UMWeb("https://www.pgyer.com/v1AgFH", "一木林", "你想要的工具，这里都有", umImage)
        ShareDialog.Builder(this)
            .setShareLink(umWeb)
            .setListener(object : UmengShare.OnShareListener {
                override fun onSucceed(platform: Platform?) {

                }
            })
            .show()
    }

    @SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btn_search -> {
                val keyWork = input_keyword?.text?.toString()
                if (keyWork.isNullOrBlank()) {
                    toast("请输入要搜索的内容")
                    return
                }
                LiveDataBus.postValue("search", keyWork)
                hideKeyboard(view)
            }

            else -> {}
        }
    }

    /**
     * 切换Fragment
     */
    private fun switchFragment() {
        val urlStr = getString(URL)
        if (!urlStr.isNullOrBlank()) {
            BrowserActivity.start(this, urlStr)
            finish()
            return
        }
        val fragment: Fragment = when (getInt(TOOLS_ID)) {
            1001 -> {
                //文字转语音
                TextToSpeechFragment()
            }

            1002 -> {
                //中文转拼音
                ChineseTurnPinyinFragment()
            }

            1003 -> {
                //数字转中文
                DigitToChineseFragment()
            }

            1004 -> {
                //拆分选词
                SplitWordChoiceFragment()
            }

            1005 -> {
                //手持弹幕
                HandHeldBarrageFragment()
            }

            2001 -> {
                //指南针
                CompassFragment()
            }

            2002 -> {
                //水平仪
                LevelFragment()
            }

            2003 -> {
                //噪音测量
                NoiseMeasurementFragment()
            }

            2005 -> {
                //查看设备信息
                DeviceDetailFragment()
            }

            2006 -> {
                //金属探测仪
                MetalDetectorFragment()
            }

            2007 -> {
                //小米时钟
                XiaomiClockFragment()
            }

            2008 -> {
                //手机应用管理
                AppManagerFragment()
            }

            2009 -> {
                //翻页时钟
                PageTurnClockFragment()
            }

            3001 -> {
                //MD5加密
                Md5EncryptFragment()
            }

            3002 -> {
                //Base64编码/解码
                Base64EncryptFragment()
            }

            3003 -> {
                //摩斯密码
                MorseCodeFragment()
            }

            3004 -> {
                //RSA加密/解密
                RsaEncryptFragment()
            }

            4001 -> {
                //邮编查询
                ZipCodeInquiryFragment()
            }

            4002 -> {
                //公司黑名单
                CompanyBlackListFragment()
            }

            4006 -> {
                //购物优惠查询
                ShengqianbaoFragment()
            }

            7001 -> {
                //图片文本
                ScannerTextFragment()
            }

            7002 -> {
                //身份证识别
                ScannerFragment(3)
            }

            7003 -> {
                //条码识别
                ScannerFragment(0)
            }

            7004 -> {
                //二维码识别
                ScannerFragment(1)
            }

            7005 -> {
                //银行卡识别
                ScannerFragment(2)
            }

            7006 -> {
                //车牌识别
                ScannerFragment(4)
            }

            7007 -> {
                //驾驶证识别
                ScannerFragment(5)
            }

            8001 -> {
                //计算器
                GeneralFragment()
            }

            8002 -> {
                //年龄星座计算
                AgeHoroscopeCalculationFragment()
            }

            8003 -> {
                //身高预测
                HeightPredictionFragment()
            }

            9001 -> {
                //定位信息获取
                LocationInfoFragment()
            }

            9002 -> {
                //路线查询
                RouteLookupFragment()
            }

            9003 -> {
                //坐标拾取
                CoordinatePickingFragment()
            }

            10001 -> {
                //汇率计算器
                ExchangeFragment()
            }

            10002 -> {
                //亲属关系计算器
                RelationFragment()
            }

            11001 -> {
                //做个选择
                MakeAChoiceFragment()
            }

            11002 -> {
                //尺子
                RulerFragment()
            }

            11003 -> {
                //分贝仪
                DecibelMeterFragment()
            }

            13001 -> {
                //QQ强制对话
                QQForcedConversationFragment()
            }

            14001 -> {
                //摸鱼日历
                TouchFishCalendarFragment()
            }

            14002 -> {
                //摸鱼日报
                TouchingTheFishDailyFragment()
            }

            14003 -> {
                //明星八卦
                StarGossipFragment()
            }

            14004 -> {
                //内涵段子
                JokeFragment()
            }

            14005 -> {
                //新闻简报
                NewsBriefFragment()
            }

            14006 -> {
                //情感花园
                EmotionGardenFragment()
            }

            14007 -> {
                //星座运势
                StarHoroscopeFragment()
            }

            15004 -> {
                if (DJXSdk.isStartSuccess().not()) {
                    Logger.e("短视频SDK初始化未完成")
                    return
                }
                DJXHolder.loadDramaHome(object : IDJXDramaHomeListener() {

                }).fragment
            }
            15005 -> {
                DPSdk.factory().createDoubleFeed(DPWidgetGridParams.obtain()).fragment
            }

            else -> {
                EmptyFragment()
            }

        }

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    private fun loadAd() {
        bannerContainer?.removeAllViews()

        /** 1、创建AdSlot对象 */
        //@[classname]
        adSlot = AdSlot.Builder()
            .setCodeId("102636040")
            .setImageAcceptedSize(ScreenUtils.getScreenWidth(), ConvertUtils.dp2px(75f)) // 单位px
            .build()

        /** 2、创建TTAdNative对象 */
        //@[classname]//@[methodname]
        adNativeLoader = TTAdSdk.getAdManager().createAdNative(this)

        /** 3、创建加载、展示监听器 */
        initListeners()

        /** 4、加载广告 */
        adNativeLoader?.loadBannerExpressAd(adSlot, nativeExpressAdListener)
    }

    private fun showAd() {
        if (bannerAd == null) {
            Logger.d("请先加载广告或等待广告加载完毕后再调用show方法")
        }
        bannerAd?.setExpressInteractionListener(expressAdInteractionListener)
        bannerAd?.setDislikeCallback(this@ToolsDetailActivity, dislikeInteractionCallback)

        /** 注意：使用融合功能时，load成功后可直接调用getExpressAdView获取广告view展示，而无需调用render等onRenderSuccess后 */
        val bannerView: View? = bannerAd?.expressAdView
        if (bannerView != null) {
            bannerContainer?.removeAllViews()
            bannerContainer?.visibility = View.VISIBLE
            bannerContainer?.addView(bannerView)
        }
    }

    private fun initListeners() {
        // 广告加载监听器
        //@[classname]
        nativeExpressAdListener = object : TTAdNative.NativeExpressAdListener {
            //@[classname]
            override fun onNativeExpressAdLoad(ads: MutableList<TTNativeExpressAd>?) {
                if (ads != null) {
                    Logger.d("banner load success: " + ads.size)
                }
                ads?.let {
                    if (it.size > 0) {
                        //@[classname]
                        val ad: TTNativeExpressAd = it[0]
                        bannerAd = ad
                    }
                }
                showAd()
            }

            override fun onError(code: Int, message: String?) {
                Logger.d("banner load fail: $code, $message")
            }
        }
        // 广告展示监听器
        expressAdInteractionListener = object :
        //@[classname]
            TTNativeExpressAd.ExpressAdInteractionListener {
            override fun onAdClicked(view: View?, type: Int) {
                Logger.d("banner clicked")
            }

            override fun onAdShow(view: View?, type: Int) {
                Logger.d("banner show")
            }

            override fun onRenderFail(view: View?, msg: String?, code: Int) {
                // 注意：使用融合功能时，无需调用render，load成功后可调用mBannerAd.getExpressAdView()进行展示。
            }

            override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                // 注意：使用融合功能时，无需调用render，load成功后可调用mBannerAd.getExpressAdView()获取view进行展示。
                // 如果调用了render，则会直接回调onRenderSuccess，***** 参数view为null，请勿使用。*****
            }
        }

        // dislike监听器，广告关闭时会回调onSelected
        //@[classname]
        dislikeInteractionCallback = object : TTAdDislike.DislikeInteractionCallback {
            override fun onShow() {
                Logger.d("banner dislike show")
            }

            override fun onSelected(
                position: Int,
                value: String?,
                enforce: Boolean
            ) {
                bannerContainer?.visibility = View.GONE
                Logger.d("banner dislike closed")
                bannerContainer?.removeAllViews()
            }

            override fun onCancel() {
                Logger.d("banner dislike cancel")
            }
        }
    }


    override fun isStatusBarEnabled(): Boolean {
        return getBoolean(IS_FULL_SCREEN)
    }

    // 销毁广告
    override fun onDestroy() {
        super.onDestroy()
        bannerAd?.destroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UmengClient.onActivityResult(this, requestCode, resultCode, data)
    }
}