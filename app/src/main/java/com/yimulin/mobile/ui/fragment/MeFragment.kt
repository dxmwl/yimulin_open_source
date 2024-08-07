package com.yimulin.mobile.ui.fragment

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.blankj.utilcode.util.ClipboardUtils
import com.hjq.base.BaseDialog
import com.hjq.base.livebus.LiveDataBus
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeTextView
import com.pdlbox.tools.utils.GlideUtils
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.app.AppFragment
import com.yimulin.mobile.http.api.AppBannerApi
import com.yimulin.mobile.http.api.UserInfoApi.UserInfoDto
import com.yimulin.mobile.http.model.HttpData
import com.yimulin.mobile.manager.UserManager
import com.yimulin.mobile.ui.activity.LoginActivity
import com.yimulin.mobile.ui.activity.MemberCenterActivity
import com.yimulin.mobile.ui.activity.ReportActivity
import com.yimulin.mobile.ui.activity.SettingActivity
import com.yimulin.mobile.ui.activity.UserFeedbackActivity
import com.yimulin.mobile.ui.adapter.AppBannerAdapter
import com.yimulin.mobile.ui.dialog.MessageDialog
import com.youth.banner.Banner


/**
 * @ClassName: LocalToolsFragment
 * @Description: 个人中心
 * @Author: 常利兵
 * @Date: 2023/5/11 22:43
 **/
class MeFragment : AppFragment<AppActivity>() {

    companion object {

        fun newInstance(): MeFragment {
            return MeFragment()
        }
    }

    private val userAvatar: ImageView? by lazy { findViewById(R.id.user_avatar) }
    private val nickName: TextView? by lazy { findViewById(R.id.nick_name) }
    private val member_time: ShapeTextView? by lazy { findViewById(R.id.member_time) }
    private val banner: Banner<AppBannerApi.BannerBean, AppBannerAdapter>? by lazy { findViewById(R.id.banner_me) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }

    override fun initView() {
        setOnClickListener(
            R.id.my_member, R.id.kefu_online, R.id.setting, R.id.nick_name,
            R.id.member_time, R.id.user_feed_back, R.id.user_tousu, R.id.join_qq_group
        )

        banner?.let {
//            it.setBannerGalleryEffect(39, 16)
            it.addBannerLifecycleObserver(this)
        }
    }

    override fun initData() {
        UserManager.userInfo?.let {
            setUserInfo(it)
        }

        LiveDataBus.subscribe("upDateUserInfo", this) { data ->
            val userInfoDto = data as UserInfoDto
            setUserInfo(userInfoDto)
        }

        getBannerList()
    }

    /**
     * 获取轮播图数据
     */
    private fun getBannerList() {
        EasyHttp.post(this)
            .api(AppBannerApi())
            .request(object : OnHttpListener<HttpData<ArrayList<AppBannerApi.BannerBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<AppBannerApi.BannerBean>>?) {
                    result?.getData()?.let {
                        if (it.isNotEmpty()) {
                            val bannerListData = ArrayList<AppBannerApi.BannerBean>()
                            it.forEach { bannerItem ->
                                if (bannerItem.bannerImg.isNotBlank()) {
                                    bannerListData.add(bannerItem)
                                }
                            }
                            banner?.setAdapter(AppBannerAdapter(bannerListData))
                            banner?.visibility = View.VISIBLE
                        } else {
                            banner?.visibility = View.GONE
                        }
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }
            })
    }

    private fun setUserInfo(userInfoDto: UserInfoDto) {
        userAvatar?.let { it1 -> GlideUtils.showRoundImg(userInfoDto.avatar, it1) }
        nickName?.text = userInfoDto.nickName
        if (userInfoDto.memberStatus) {
            member_time?.text = userInfoDto.memberTime
        } else {
            member_time?.text = "开通会员"
        }
    }

    @SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.join_qq_group -> {
                joinQQGroup("21dZ0fuzzoKIem_VdjHIuQ6U_bi-uO7J")
            }

            R.id.user_feed_back -> {
                startActivity(UserFeedbackActivity::class.java)
            }

            R.id.user_tousu -> {
                startActivity(ReportActivity::class.java)
            }

            R.id.nick_name -> {
                if (UserManager.userInfo == null) {
                    startActivity(LoginActivity::class.java)
                }
            }

            R.id.my_member, R.id.member_time -> {
                if (UserManager.userInfo == null) {
                    startActivity(LoginActivity::class.java)
                    return
                }
                startActivity(MemberCenterActivity::class.java)
            }

            R.id.kefu_online -> {
                MessageDialog.Builder(requireContext())
                    .setTitle("在线客服")
                    .setMessage("与客服一对一沟通需求，方便、快捷地说出你的需求\n微信：dxmcpjl")
                    .setConfirm("复制微信")
                    .setListener(object : MessageDialog.OnListener {
                        override fun onConfirm(dialog: BaseDialog?) {
                            ClipboardUtils.copyText("dxmcpjl")
                            toast("复制成功")
                        }
                    })
                    .show()
            }

            R.id.setting -> {
                if (UserManager.userInfo == null) {
                    startActivity(LoginActivity::class.java)
                    return
                }
                startActivity(SettingActivity::class.java)
            }

            else -> {}
        }
    }

    /****************
     *
     * 发起添加群流程。群号：天天省钱福利①群(241404191) 的 key 为： 21dZ0fuzzoKIem_VdjHIuQ6U_bi-uO7J
     * 调用 joinQQGroup(21dZ0fuzzoKIem_VdjHIuQ6U_bi-uO7J) 即可发起手Q客户端申请加群 天天省钱福利①群(241404191)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     */
    private fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$key"))
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            startActivity(intent)
            true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            toast("未安装手Q或安装的版本不支持")
            false
        }
    }
}