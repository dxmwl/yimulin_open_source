package com.yimulin.mobile.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.widget.layout.SettingBar
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.basic.PictureSelectionModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnGridItemSelectAnimListener
import com.luck.picture.lib.interfaces.OnQueryFilterListener
import com.luck.picture.lib.interfaces.OnSelectAnimListener
import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.SelectMainStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.luck.picture.lib.utils.DensityUtil
import com.luck.picture.lib.utils.MediaUtils
import com.luck.picture.lib.utils.PictureFileUtils
import com.lxj.xpopup.XPopup
import com.orhanobut.logger.Logger
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.http.api.ReportCompanyApi
import com.yimulin.mobile.http.model.HttpData
import com.yimulin.mobile.manager.FullyGridLayoutManager
import com.yimulin.mobile.ui.adapter.GridImageAdapter
import com.yimulin.mobile.ui.popup.ReportBottomPopu
import com.yimulin.mobile.utils.*

/**
 * @ClassName: ReportCompanyActivity
 * @Description: 举报公司
 * @Author: 常利兵
 * @Date: 2023/7/30 22:05
 **/
class ReportCompanyActivity: AppActivity() {

    //举报原因
    private var reportReasonStr = ""

    private val recycler: RecyclerView? by lazy { findViewById(R.id.recycler) }
    private val chooseType: SettingBar? by lazy { findViewById(R.id.choose_type) }
    private val input_tyxydm: EditText? by lazy { findViewById(R.id.input_tyxydm) }
    private val input_company_name: EditText? by lazy { findViewById(R.id.input_company_name) }
    private val input_fr_name: EditText? by lazy { findViewById(R.id.input_fr_name) }
    private val input_wfnr: EditText? by lazy { findViewById(R.id.input_wfnr) }
    private lateinit var mAdapter: GridImageAdapter
    private val maxSelectNum = 9
    private val maxSelectVideoNum = 1
    private val mData: List<LocalMedia> = ArrayList()
    private lateinit var selectorStyle: PictureSelectorStyle
    private var launcherResult: ActivityResultLauncher<Intent?>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_report_company
    }

    override fun initView() {

        setOnClickListener(R.id.choose_type)

        initSelectorUi()

        // 注册需要写在onCreate或Fragment onAttach里，否则会报java.lang.IllegalStateException异常
        launcherResult = createActivityResultLauncher()

        recycler?.also {
            val manager = FullyGridLayoutManager(
                this,
                4, GridLayoutManager.VERTICAL, false
            )
            it.layoutManager = manager
            val itemAnimator: RecyclerView.ItemAnimator? = it.itemAnimator
            if (itemAnimator != null) {
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
                    false
            }
            it.addItemDecoration(
                GridSpacingItemDecoration(
                    4,
                    DensityUtil.dip2px(this, 8f), false
                )
            )
            mAdapter = GridImageAdapter(getContext(), mData)
            mAdapter.selectMax = maxSelectNum + maxSelectVideoNum
            it.setAdapter(mAdapter)
        }

        mAdapter.setOnItemClickListener(object : GridImageAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                // 预览图片、视频、音频
                PictureSelector.create(this@ReportCompanyActivity)
                    .openPreview()
                    .setVideoPlayerEngine(ExoPlayerEngine())
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setSelectorUIStyle(selectorStyle)
                    .startActivityPreview(position, true, mAdapter.data);
            }

            override fun openPicture() {
                //判断选中的内容里面的内容都有哪些
                var chooseMode = SelectMimeType.ofAll()
                val localMedia = mAdapter.data
                if (localMedia != null && localMedia.size > 0) {
                    val localMedia1 = localMedia[0]
                    chooseMode = if (localMedia1.mimeType.startsWith("video")) {
                        SelectMimeType.ofVideo()
                    } else {
                        SelectMimeType.ofImage()
                    }
                }

                // 进入相册
                val selectionModel: PictureSelectionModel = PictureSelector.create(getContext())
                    .openGallery(chooseMode)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setSelectorUIStyle(selectorStyle)
                    .setVideoPlayerEngine(ExoPlayerEngine())
                    .setCompressEngine(ImageFileCompressEngine())
                    .setSandboxFileEngine(MeSandboxFileEngine())
                    .isPageSyncAlbumCount(true)
                    .isWithSelectVideoImage(false)
                    .setQueryFilterListener(object : OnQueryFilterListener {
                        override fun onFilter(media: LocalMedia?): Boolean {

                            return false
                        }
                    }) //.setExtendLoaderEngine(getExtendLoaderEngine())
                    .setGridItemSelectAnimListener(OnGridItemSelectAnimListener { view, isSelected ->
                        val set = AnimatorSet()
                        set.playTogether(
                            ObjectAnimator.ofFloat(
                                view,
                                "scaleX",
                                if (isSelected) 1f else 1.12f,
                                if (isSelected) 1.12f else 1.0f
                            ),
                            ObjectAnimator.ofFloat(
                                view,
                                "scaleY",
                                if (isSelected) 1f else 1.12f,
                                if (isSelected) 1.12f else 1.0f
                            )
                        )
                        set.duration = 350
                        set.start()
                    }
                    )
                    .setSelectAnimListener(OnSelectAnimListener { view ->
                        val animation =
                            AnimationUtils.loadAnimation(getContext(), R.anim.ps_anim_modal_in)
                        view.startAnimation(animation)
                        animation.duration
                    }) //.setQueryOnlyMimeType(PictureMimeType.ofGIF())
                    .isDirectReturnSingle(true)
                    .setMaxSelectNum(maxSelectNum)
                    .setMaxVideoSelectNum(maxSelectVideoNum)
                    .setRecyclerAnimationMode(AnimationType.SLIDE_IN_BOTTOM_ANIMATION)
                    .setSelectedData(mAdapter.data)

                if (chooseMode == SelectMimeType.ofVideo()) {
                    selectionModel.setSelectionMode(SelectModeConfig.SINGLE)
                } else {
                    selectionModel.setSelectionMode(SelectModeConfig.MULTIPLE)
                }

                selectionModel.forResult(launcherResult)
            }
        })
    }

    /**
     * 创建一个ActivityResultLauncher
     *
     * @return
     */
    private fun createActivityResultLauncher(): ActivityResultLauncher<Intent?>? {
        return registerForActivityResult<Intent?, ActivityResult>(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult?> {
                override fun onActivityResult(result: ActivityResult?) {
                    val resultCode = result?.resultCode
                    if (resultCode == RESULT_OK) {
                        val selectList = PictureSelector.obtainSelectorList(result?.data)
                        analyticalSelectResults(selectList)
                    } else if (resultCode == RESULT_CANCELED) {
                        Logger.d("onActivityResult PictureSelector Cancel")
                    }
                }
            })
    }


    /**
     * 处理选择结果
     *
     * @param result
     */
    private fun analyticalSelectResults(result: java.util.ArrayList<LocalMedia>) {
        for (media in result) {
            if (media.width == 0 || media.height == 0) {
                if (PictureMimeType.isHasImage(media.mimeType)) {
                    val imageExtraInfo = MediaUtils.getImageSize(getContext(), media.path)
                    media.width = imageExtraInfo.width
                    media.height = imageExtraInfo.height
                } else if (PictureMimeType.isHasVideo(media.mimeType)) {
                    val videoExtraInfo = MediaUtils.getVideoSize(getContext(), media.path)
                    media.width = videoExtraInfo.width
                    media.height = videoExtraInfo.height
                }
            }
            Logger.d("文件名: " + media.fileName)
            Logger.d("是否压缩:" + media.isCompressed)
            Logger.d("压缩:" + media.compressPath)
            Logger.d("初始路径:" + media.path)
            Logger.d("绝对路径:" + media.realPath)
            Logger.d("是否裁剪:" + media.isCut)
            Logger.d("裁剪路径:" + media.cutPath)
            Logger.d("是否开启原图:" + media.isOriginal)
            Logger.d("原图路径:" + media.originalPath)
            Logger.d("沙盒路径:" + media.sandboxPath)
            Logger.d("水印路径:" + media.watermarkPath)
            Logger.d("视频缩略图:" + media.videoThumbnailPath)
            Logger.d("原始宽高: " + media.width + "x" + media.height)
            Logger.d("裁剪宽高: " + media.cropImageWidth + "x" + media.cropImageHeight)
            Logger.d("文件大小: " + PictureFileUtils.formatAccurateUnitFileSize(media.size))
            Logger.d("文件时长: " + media.duration)
        }
        runOnUiThread {
            val isMaxSize = result.size == mAdapter.selectMax
            val oldSize: Int = mAdapter.data.size
            mAdapter.notifyItemRangeRemoved(0, if (isMaxSize) oldSize + 1 else oldSize)
            mAdapter.data.clear()
            mAdapter.data.addAll(result)
            mAdapter.notifyItemRangeInserted(0, result.size)
        }
    }

    /**
     * 初始化选择器UI
     */
    private fun initSelectorUi() {
        selectorStyle = PictureSelectorStyle()
        val whiteTitleBarStyle = TitleBarStyle()
        whiteTitleBarStyle.titleBackgroundColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_white)
        whiteTitleBarStyle.titleDrawableRightResource = R.drawable.ic_orange_arrow_down
        whiteTitleBarStyle.titleLeftBackResource = R.drawable.ps_ic_black_back
        whiteTitleBarStyle.titleTextColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_black)
        whiteTitleBarStyle.titleCancelTextColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_53575e)
        whiteTitleBarStyle.isDisplayTitleBarLine = true

        val whiteBottomNavBarStyle = BottomNavBarStyle()
        whiteBottomNavBarStyle.bottomNarBarBackgroundColor = Color.parseColor("#EEEEEE")
        whiteBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_53575e)

        whiteBottomNavBarStyle.bottomPreviewNormalTextColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_9b)
        whiteBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_fa632d)
        whiteBottomNavBarStyle.isCompleteCountTips = false
        whiteBottomNavBarStyle.bottomEditorTextColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_53575e)
        whiteBottomNavBarStyle.bottomOriginalTextColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_53575e)

        val selectMainStyle = SelectMainStyle()
        selectMainStyle.statusBarColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_white)
        selectMainStyle.isDarkStatusBarBlack = true
        selectMainStyle.selectNormalTextColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_9b)
        selectMainStyle.selectTextColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_fa632d)
        selectMainStyle.previewSelectBackground = R.drawable.ps_demo_white_preview_selector
        selectMainStyle.selectBackground = R.drawable.ps_checkbox_selector
        selectMainStyle.selectText = getString(R.string.ps_done_front_num)
        selectMainStyle.mainListBackgroundColor =
            ContextCompat.getColor(getContext(), R.color.ps_color_white)

        selectorStyle.titleBarStyle = whiteTitleBarStyle
        selectorStyle.bottomBarStyle = whiteBottomNavBarStyle
        selectorStyle.selectMainStyle = selectMainStyle
    }

    override fun initData() {
        
    }

    @SingleClick
    override fun onRightClick(view: View) {
        commitInfo()
    }

    private fun commitInfo() {
        showDialog()
        val localMedia = mAdapter.data
        val tempImgs = ArrayList<Any>()
        for (media in localMedia) {
            val path = media.availablePath
            tempImgs.add(
                if (PictureMimeType.isContent(path) && !media.isCut && !media.isCompressed) Uri.parse(
                    path
                ) else path
            )
        }
        val imgList = ArrayList<String>()
        CosManager.multiFileUpload(
            tempImgs,
            CosManager.CosType.REPORT_COMPANY
        ) { status, urls ->
            if (!status) {
                hideDialog()
                toast("图片上传失败,请稍后再试")
                return@multiFileUpload
            }
            imgList.addAll(urls)
            EasyHttp.post(this)
                .api(ReportCompanyApi().apply {
                    companyNumber = input_tyxydm?.text?.toString()
                    companyName = input_company_name?.text?.toString()
                    legalRepresentative = input_fr_name?.text?.toString()
                    reportType = reportReasonStr
                    reportReason = input_wfnr?.text?.toString()
                    reportImgs =
                        imgList.toString().replace("[", "").replace("]", "").replace(" ", "")
                })
                .request(object : OnHttpListener<HttpData<Any>> {
                    override fun onSucceed(result: HttpData<Any>?) {
                        hideDialog()
                        toast("提交成功")
                        finish()
                    }

                    override fun onFail(e: Exception?) {
                        hideDialog()
                        toast(e?.message)
                    }
                })
        }
    }

    @SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.choose_type -> {
                XPopup.Builder(getContext())
                    .moveUpToKeyboard(false)
                    .asCustom(ReportBottomPopu(getContext()) {
                        chooseType?.setRightText(it)
                        reportReasonStr = it
                    })
                    .show()
            }
            else -> {}
        }
    }
}