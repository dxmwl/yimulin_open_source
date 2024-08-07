package com.yimulin.mobile.ui.fragment

import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.Glide
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.toast.ToastUtils
import com.hjq.umeng.Platform
import com.hjq.umeng.UmengShare
import com.umeng.socialize.media.UMImage
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.http.api.GetJokeApi
import com.yimulin.mobile.http.api.GetNewsBriefApi
import com.yimulin.mobile.http.model.HttpData
import com.yimulin.mobile.ui.dialog.ShareDialog

/**
 * @ClassName: NewsBriefFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/1/17 0017 15:18
 **/
class NewsBriefFragment: AppFragment<AppActivity>() {

    private val iv_result: ImageView? by lazy { findViewById(R.id.iv_result) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_touch_fish_calendar
    }

    override fun initView() {
        setOnClickListener(R.id.btn_share)
    }

    override fun initData() {
        getNewsBrief()
    }

    private fun getNewsBrief() {
        EasyHttp.get(this)
            .api(GetNewsBriefApi())
            .request(object : HttpCallback<HttpData<String>>(this) {
                override fun onSucceed(result: HttpData<String>?) {
                    iv_result?.let {
                        Glide.with(this@NewsBriefFragment)
                            .load(result?.getData())
                            .into(it)
                    }
                }
            })
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_share -> {
                val view2Bitmap = ConvertUtils.view2Bitmap(iv_result)
                val umImage = UMImage(requireContext(), view2Bitmap)
                ShareDialog.Builder(requireActivity())
                    .setShareImage(umImage)
                    .setListener(object : UmengShare.OnShareListener {

                        override fun onSucceed(platform: Platform?) {
                            ToastUtils.show("分享成功")
                        }

                        override fun onError(platform: Platform?, t: Throwable) {
                            ToastUtils.show(t.message)
                        }

                        override fun onCancel(platform: Platform?) {
                            ToastUtils.show("分享取消")
                        }
                    })
                    .show()
            }
            else -> {}
        }
    }
}