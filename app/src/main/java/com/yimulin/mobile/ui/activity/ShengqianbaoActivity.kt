package com.yimulin.mobile.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.shape.view.ShapeEditText
import com.umeng.socialize.media.UMWeb
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.http.api.GetYouhuiApi
import com.yimulin.mobile.http.model.HttpData
import com.yimulin.mobile.other.PermissionInterceptor
import com.yimulin.mobile.ui.dialog.ShareDialog

/**
 * 省钱宝
 */
class ShengqianbaoActivity : AppActivity() {

    private var youhuiUrl: String = ""
    private val shapeEditText: ShapeEditText? by lazy { findViewById(R.id.shapeEditText) }
    private val textView2: TextView? by lazy { findViewById(R.id.textView2) }
    private val shiji_pay: TextView? by lazy { findViewById(R.id.shiji_pay) }
    private val yuanjia: TextView? by lazy { findViewById(R.id.yuanjia) }
    private val youhuiquan: TextView? by lazy { findViewById(R.id.youhuiquan) }
    private val go_buy: TextView? by lazy { findViewById(R.id.go_buy) }

    override fun getLayoutId(): Int {
        return R.layout.activity_shengqianbao
    }

    private var inputTklStr = ""

    override fun initView() {
        setOnClickListener(R.id.go_buy, R.id.shapeTextView, R.id.clear_input, R.id.btn_share)

        shapeEditText?.addTextChangedListener {
            inputTklStr = it?.toString().toString()
        }

        postDelayed({
            val clipboardContent = ClipboardUtils.getText()
            if (clipboardContent.isNullOrBlank().not()) {
                inputTklStr = clipboardContent.toString()
                shapeEditText?.setText(inputTklStr)
                shapeEditText?.setSelection(inputTklStr.length)
                getYouhuiInfo()
            }
        }, 2000)
    }

    override fun initData() {
        getYouhuiInfo()
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btn_share -> {
                getYouhuiInfo(true)
            }
            R.id.go_buy -> {
                BrowserActivity.start(this@ShengqianbaoActivity, youhuiUrl)
            }
            R.id.clear_input -> {
                shapeEditText?.text?.clear()
            }
            R.id.shapeTextView -> {
                getYouhuiInfo()
            }
            else -> {}
        }
    }

    private fun getYouhuiInfo(needShare: Boolean = false) {
        if (inputTklStr.isBlank()) {
            toast("请将商品链接到输入框")
            return
        }
        showDialog()
        EasyHttp.post(this)
            .api(GetYouhuiApi().apply {
                content = inputTklStr
            })
            .request(object : OnHttpListener<HttpData<GetYouhuiApi.GoodsYouhuiDto>> {
                override fun onSucceed(result: HttpData<GetYouhuiApi.GoodsYouhuiDto>?) {
                    hideDialog()
                    result?.getData()?.let {
                        youhuiUrl = it.couponLink
                        yuanjia?.text = it.originPrice
                        textView2?.text = it.title
                        youhuiquan?.text = "-${it.couponsPrice}"
                        shiji_pay?.text = it.currentPrice
                        go_buy?.text = "立即购买省\n¥${it.couponsPrice}"

                        if (needShare) {
                            XXPermissions.with(this@ShengqianbaoActivity)
                                .interceptor(PermissionInterceptor("使用存储权限,用于分享链接/图片等到QQ/微信等平台"))
                                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                                .request { _, all ->
                                    if (all) {
                                        val umWeb = UMWeb(youhuiUrl)
                                        umWeb.title = "粉丝福利购"
                                        umWeb.description = "点击领取福利"
                                        ShareDialog.Builder(this@ShengqianbaoActivity)
                                            .setShareLink(umWeb)
                                            .show()
                                    }
                                }
                        }
                    }
                }

                override fun onFail(e: Exception?) {
                    hideDialog()
                    toast("该商品暂无相关优惠,可以试试其他商品哦~")
                }
            })
    }
}