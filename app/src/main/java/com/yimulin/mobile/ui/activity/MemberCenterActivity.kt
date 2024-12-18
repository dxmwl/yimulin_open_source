//package com.yimulin.mobile.ui.activity
//
//import android.os.Handler
//import android.os.Message
//import android.text.TextUtils
//import android.view.View
//import android.widget.ImageView
//import android.widget.RadioGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.alipay.sdk.app.PayTask
//import com.hjq.base.BaseAdapter
//import com.hjq.http.EasyHttp
//import com.hjq.http.listener.OnHttpListener
//import com.hjq.shape.view.ShapeTextView
//import com.hjq.toast.ToastUtils
//import com.orhanobut.logger.Logger
//import com.pdlbox.tools.utils.GlideUtils
//import com.tencent.mm.opensdk.modelbase.BaseResp
//import com.tencent.mm.opensdk.modelpay.PayReq
//import com.tencent.mm.opensdk.openapi.IWXAPI
//import com.tencent.mm.opensdk.openapi.WXAPIFactory
//import com.yimulin.mobile.aop.SingleClick
//import com.yimulin.mobile.app.AppActivity
//import com.yimulin.mobile.R
//import com.yimulin.mobile.http.api.CreateMemberOrderApi
//import com.yimulin.mobile.http.api.GetPayInfoApi
//import com.yimulin.mobile.http.api.GetPayListApi
//import com.yimulin.mobile.http.api.UserInfoApi
//import com.yimulin.mobile.http.model.HttpData
//import com.yimulin.mobile.http.model.PayResult
//import com.yimulin.mobile.http.model.WxPayMsg
//import com.hjq.base.livebus.LiveDataBus
//import com.yimulin.mobile.manager.UserManager
//import com.yimulin.mobile.ui.adapter.PayListAdapter
//import java.lang.Exception
//
///**
// * @ClassName: MemberCenterActivity
// * @Description:
// * @Author: 常利兵
// * @Date: 2023/5/26 17:53
// **/
//class MemberCenterActivity: AppActivity() {
//
//    private lateinit var orderInfo: String
//    private var weChatApi: IWXAPI? = null
//    private lateinit var payListAdapter: PayListAdapter
//    private val payList:RecyclerView? by lazy { findViewById(R.id.pay_list) }
//    private val rg_pay:RadioGroup? by lazy { findViewById(R.id.rg_pay) }
//    private val userAvatar: ImageView? by lazy { findViewById(R.id.user_avatar) }
//    private val nickName: TextView? by lazy { findViewById(R.id.nick_name) }
//    private val member_time: ShapeTextView? by lazy { findViewById(R.id.member_time) }
//    private var payType = 1
//    private var payListId = ""
//
//    override fun getLayoutId(): Int {
//        return R.layout.activity_member_center
//    }
//
//    override fun initView() {
//        //注册微信
//        weChatApi = WXAPIFactory.createWXAPI(this, null)
//        weChatApi?.registerApp("wx167f9827a1280219")
//
//        setOnClickListener(R.id.btn_pay)
//
//        payList?.also { it ->
//            it.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
//            payListAdapter = PayListAdapter(this)
//            payListAdapter.setOnItemClickListener(object :BaseAdapter.OnItemClickListener{
//                override fun onItemClick(
//                    recyclerView: RecyclerView?,
//                    itemView: View?,
//                    position: Int
//                ) {
//                    payListAdapter.getData().forEachIndexed { index, paylistDto ->
//                        paylistDto.checked = false
//                    }
//
//                    payListAdapter.getItem(position).checked = true
//                    payListId = payListAdapter.getItem(position).payListId
//                    payListAdapter.notifyDataSetChanged()
//                }
//            })
//            it.adapter = payListAdapter
//        }
//
//        rg_pay?.setOnCheckedChangeListener { group, checkedId ->
//            when (checkedId) {
//                R.id.rb_wechat_pay -> {
//                    payType = 1
//                }
//                R.id.rb_ali_pay -> {
//                    payType = 2
//                }
//                else -> {}
//            }
//        }
//
//        LiveDataBus.subscribe("SendWechatPayInfo",this){ data->
//            val wxPayMsg = data as WxPayMsg
//            if (wxPayMsg.resp.errCode == BaseResp.ErrCode.ERR_OK) {
//                val msg = Message()
//                msg.what = 1
//                mHandler.sendMessage(msg)
//            }
//        }
//    }
//
//    override fun initData() {
//        UserManager.userInfo?.let {
//            setUserInfo(it)
//        }
//
//        getPayList()
//    }
//
//    private fun getPayList() {
//        EasyHttp.get(this)
//            .api(GetPayListApi())
//            .request(object : OnHttpListener<HttpData<ArrayList<GetPayListApi.PaylistDto>>>{
//                override fun onSucceed(result: HttpData<ArrayList<GetPayListApi.PaylistDto>>?) {
//                    result?.getData()?.let {
//                        if (it.size==0) return@let
//                        it[0].checked = true
//                        payListId = it[0].payListId
//                        payListAdapter.setData(it)
//                    }
//                }
//
//                override fun onFail(e: Exception?) {
//                    toast(e?.message)
//                }
//            })
//    }
//
//    private fun setUserInfo(userInfoDto: UserInfoApi.UserInfoDto) {
//        userAvatar?.let { it1 -> GlideUtils.showRoundImg(userInfoDto.avatar, it1) }
//        nickName?.text = userInfoDto.nickName
//        if (userInfoDto.memberStatus) {
//            member_time?.text = userInfoDto.memberTime
//        } else {
//            member_time?.text = "开通会员"
//        }
//    }
//
//
//    @SingleClick
//    override fun onClick(view: View) {
//        super.onClick(view)
//        when (view.id) {
//            R.id.btn_pay -> {
//                EasyHttp.post(this)
//                    .api(CreateMemberOrderApi().apply {
//                        payListId = this@MemberCenterActivity.payListId
//                    })
//                    .request(object :OnHttpListener<HttpData<CreateMemberOrderApi.PayOrderInfoDto>>{
//                        override fun onSucceed(result: HttpData<CreateMemberOrderApi.PayOrderInfoDto>?) {
//                            result?.getData()?.let {
//                                getPayInfo(it.orderNumber)
//                            }
//                        }
//
//                        override fun onFail(e: Exception?) {
//                            toast(e?.message)
//                        }
//
//                    })
//            }
//            else -> {}
//        }
//    }
//
//    /**
//     * 获取预支付信息
//     */
//    private fun getPayInfo(orderNumber: String) {
//        EasyHttp.post(this)
//            .api(GetPayInfoApi().apply {
//                this.orderNumber = orderNumber
//                type = payType
//            })
//            .request(object :OnHttpListener<HttpData<GetPayInfoApi.PayInfoDto>>{
//                override fun onSucceed(result: HttpData<GetPayInfoApi.PayInfoDto>?) {
//                    result?.getData()?.let {
//                        if (payType==1){
//                            weChatPay(it)
//                        }else if (payType==2){
//                            aliPay(it)
//                        }
//                    }
//                }
//
//                override fun onFail(e: Exception?) {
//                    toast(e?.message)
//                }
//
//            })
//
//    }
//
//    /**
//     * 支付宝支付
//     */
//    private fun aliPay(it: GetPayInfoApi.PayInfoDto) {
//        orderInfo = it.aliPayOrderInfo
//        val payThread = Thread(payRunnable)
//        payThread.start()
//    }
//
//    /**
//     * 调用微信支付
//     */
//    private fun weChatPay(it: GetPayInfoApi.PayInfoDto) {
//        val request = PayReq()
//        val wechat_result = it.wechatPayInfo
//        request.appId = wechat_result.appid
//        request.partnerId = wechat_result.partnerid
//        request.prepayId = wechat_result.prepayId
//        request.packageValue = wechat_result.packageValue
//        request.nonceStr = wechat_result.noncestr
//        request.timeStamp = wechat_result.timestamp
//        request.sign = wechat_result.sign
//        weChatApi?.sendReq(request)
//    }
//
//    var payRunnable: Runnable? = Runnable {
//        val alipay = PayTask(this@MemberCenterActivity)
//        val result = alipay.payV2(orderInfo, true)
//        val msg = Message()
//        msg.what = 2
//        msg.obj = result
//        mHandler.sendMessage(msg)
//    }
//
//    private val mHandler = Handler { msg ->
//        if (msg.what == 2) {
//            //支付宝支付
//            val payResult =
//                PayResult(msg.obj as Map<String?, String?>)
//
//            val resultStatus = payResult.resultStatus
//            // 判断resultStatus 为9000则代表支付成功
//            if (TextUtils.equals(resultStatus, "9000")) {
//                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                ToastUtils.show("支付成功")
//            } else if (TextUtils.equals(resultStatus, "6001")) {
//                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                ToastUtils.show("取消支付")
//            } else {
//                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                ToastUtils.show("支付失败")
//            }
//            Logger.d("支付宝支付结果:" + msg.obj)
//        } else if (msg.what == 1) {
//            //微信支付
//            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//            ToastUtils.show("支付成功")}
//        false
//    }
//}