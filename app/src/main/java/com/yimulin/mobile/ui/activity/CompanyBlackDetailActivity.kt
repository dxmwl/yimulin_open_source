package com.yimulin.mobile.ui.activity

import android.view.View
import android.widget.TextView
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.http.api.GetCompanyBlackByIdApi
import com.yimulin.mobile.http.api.SearchCompanyBlackApi
import com.yimulin.mobile.http.model.HttpData
import java.lang.Exception
import java.util.ArrayList

/**
 * @ClassName: CompanyBlackDetailActivity
 * @Description:公司黑名单详情
 * @Author: 常利兵
 * @Date: 2023/7/31 15:56
 **/
class CompanyBlackDetailActivity : AppActivity(), BGANinePhotoLayout.Delegate {

    private val nine_img_view: BGANinePhotoLayout? by lazy { findViewById(R.id.nine_img_view) }
    private val company_name: TextView? by lazy { findViewById(R.id.company_name) }
    private val company_number: TextView? by lazy { findViewById(R.id.company_number) }
    private val user_name: TextView? by lazy { findViewById(R.id.user_name) }
    private val report_type: TextView? by lazy { findViewById(R.id.report_type) }
    private val report_reason: TextView? by lazy { findViewById(R.id.report_reason) }
    private var companyBlackId: String? = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_company_black_detail
    }

    override fun initView() {
        companyBlackId = getString("id")
        nine_img_view?.setDelegate(this)
    }

    override fun initData() {
        getCompanyBlackById(companyBlackId)
    }

    private fun getCompanyBlackById(companyBlackId: String?) {
        EasyHttp.post(this)
            .api(GetCompanyBlackByIdApi().apply {
                this.companyBlackId = companyBlackId
            })
            .request(object : OnHttpListener<HttpData<SearchCompanyBlackApi.CompanyBlackDto>> {
                override fun onSucceed(result: HttpData<SearchCompanyBlackApi.CompanyBlackDto>?) {
                    result?.getData()?.let {
                        company_name?.text = it.companyName
                        company_number?.text = it.companyNumber
                        user_name?.text = it.legalRepresentative
                        report_type?.text = it.reportType
                        report_reason?.text = it.reportReason
                        if (it.reportImgs.isNullOrBlank().not()) {
                            val imgList = it.reportImgs?.split(",")
                            nine_img_view?.data = imgList?.let { it1 -> ArrayList(it1) }
                        }
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }

            })
    }

    override fun onClickNinePhotoItem(
        ninePhotoLayout: BGANinePhotoLayout?,
        view: View?,
        position: Int,
        model: String?,
        models: MutableList<String>?
    ) {
        if (ninePhotoLayout != null) {
            val mContext = ninePhotoLayout.context
            val previewActivity = BGAPhotoPreviewActivity.IntentBuilder(mContext)
            if (ninePhotoLayout.itemCount == 1) {
                // 预览单张图片
                previewActivity.previewPhoto(ninePhotoLayout.currentClickItem)
            } else if (ninePhotoLayout.itemCount > 1) {
                // 预览多张图片
                previewActivity.previewPhotos(ninePhotoLayout.data)
                    .currentPosition(ninePhotoLayout.currentClickItemPosition) // 当前预览图片的索引
            }
            mContext.startActivity(previewActivity.build())
        }
    }

    override fun onClickExpand(
        ninePhotoLayout: BGANinePhotoLayout?,
        view: View?,
        position: Int,
        model: String?,
        models: MutableList<String>?
    ) {
        ninePhotoLayout?.setIsExpand(true)
        ninePhotoLayout?.flushItems()
    }
}