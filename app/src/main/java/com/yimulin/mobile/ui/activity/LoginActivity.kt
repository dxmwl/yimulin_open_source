package com.yimulin.mobile.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.base.BaseDialog
import com.hjq.http.EasyConfig
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.umeng.Platform
import com.hjq.umeng.UmengClient
import com.hjq.widget.view.CountdownView
import com.hjq.widget.view.SubmitButton
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.Log
import com.yimulin.mobile.http.api.GetCodeApi
import com.yimulin.mobile.http.api.LoginApi
import com.yimulin.mobile.http.model.HttpData
import com.hjq.base.livebus.LiveDataBus
import com.hjq.http.listener.HttpCallback
import com.yimulin.mobile.http.api.PwdLoginApi
import com.yimulin.mobile.manager.InputTextManager
import com.yimulin.mobile.manager.UserManager
import com.yimulin.mobile.other.AppConfig
import com.yimulin.mobile.other.KeyboardWatcher
import com.yimulin.mobile.ui.dialog.MessageDialog
import okhttp3.Call

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 登录界面
 */
class LoginActivity : AppActivity(),
    KeyboardWatcher.SoftKeyboardStateListener, TextView.OnEditorActionListener{

    companion object {

        private const val INTENT_KEY_IN_PHONE: String = "phone"
        private const val INTENT_KEY_IN_PASSWORD: String = "password"

        @Log
        fun start(context: Context, phone: String?, password: String?) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_PHONE, phone)
            intent.putExtra(INTENT_KEY_IN_PASSWORD, password)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val countdownView: CountdownView? by lazy { findViewById(R.id.cv_register_countdown) }
    private val logoView: ImageView? by lazy { findViewById(R.id.iv_login_logo) }
    private val bodyLayout: ViewGroup? by lazy { findViewById(R.id.ll_login_body) }
    private val phoneView: EditText? by lazy { findViewById(R.id.et_login_phone) }
    private val passwordView: EditText? by lazy { findViewById(R.id.et_login_password) }
    private val loginType: TextView? by lazy { findViewById(R.id.login_type) }
    private val xieyi_str: TextView? by lazy { findViewById(R.id.xieyi_str) }
    private val cb_xieyi: CheckBox? by lazy { findViewById(R.id.cb_xieyi) }
    private val forgetView: View? by lazy { findViewById(R.id.tv_login_forget) }
    private val commitView: SubmitButton? by lazy { findViewById(R.id.btn_login_commit) }
    private val otherView: View? by lazy { findViewById(R.id.ll_login_other) }
    private val qqView: View? by lazy { findViewById(R.id.iv_login_qq) }
    private val weChatView: View? by lazy { findViewById(R.id.iv_login_wechat) }
    private val codeView: EditText? by lazy { findViewById(R.id.et_register_code) }
    private val login_type_pwd: LinearLayout? by lazy { findViewById(R.id.login_type_pwd) }
    private val login_type_code: LinearLayout? by lazy { findViewById(R.id.login_type_code) }
    private val et_login_password: EditText? by lazy { findViewById(R.id.et_login_password) }

    /** logo 缩放比例 */
    private val logoScale: Float = 0.8f

    /** 动画时间 */
    private val animTime: Int = 300

    /**
     * 当前登录类型 1:验证码登录 2:密码登录
     */
    private var loginTypeCode = 1

    override fun getLayoutId(): Int {
        return R.layout.login_activity
    }

    override fun initView() {
        setOnClickListener(forgetView, commitView, qqView, weChatView, countdownView, loginType)
        passwordView?.setOnEditorActionListener(this)
        commitView?.let {
            InputTextManager.with(this)
                .addView(phoneView)
                .addView(codeView)
                .setMain(it)
                .build()
        }

        SpanUtils.with(xieyi_str)
            .append("《用户协议》")
            .setClickSpan(Color.parseColor("#43CF7C"), false) {
                BrowserActivity.start(this, AppConfig.getUserAgreement())
            }
            .append("及")
            .append("《隐私政策》")
            .setClickSpan(Color.parseColor("#43CF7C"), false) {
                BrowserActivity.start(this, AppConfig.getPrivacyPolicy())
            }.create()

    }

    override fun initData() {
        postDelayed({
            KeyboardWatcher.with(this@LoginActivity)
                .setListener(this@LoginActivity)
        }, 500)

        // 判断用户当前有没有安装 QQ
        if (!UmengClient.isAppInstalled(this, Platform.QQ)) {
            qqView?.visibility = View.GONE
        }

        // 判断用户当前有没有安装微信
        if (!UmengClient.isAppInstalled(this, Platform.WECHAT)) {
            weChatView?.visibility = View.GONE
        }

        // 如果这两个都没有安装就隐藏提示
        if (qqView?.visibility == View.GONE && weChatView?.visibility == View.GONE) {
            otherView?.visibility = View.GONE
        }

        // 自动填充手机号和密码
        phoneView?.setText(getString(INTENT_KEY_IN_PHONE))
        passwordView?.setText(getString(INTENT_KEY_IN_PASSWORD))
    }

    @SingleClick
    override fun onClick(view: View) {
        if (view === countdownView) {
            if (phoneView?.text.toString().length != 11) {
                phoneView?.startAnimation(
                    AnimationUtils.loadAnimation(
                        getContext(),
                        R.anim.shake_anim
                    )
                )
                toast(R.string.common_phone_input_error)
                return
            }
            // 获取验证码
            EasyHttp.post(this)
                .api(GetCodeApi().apply {
                    setPhone(phoneView?.text.toString())
                    type = 1
                })
                .request(object : OnHttpListener<HttpData<Void?>> {

                    override fun onSucceed(data: HttpData<Void?>) {
                        toast(R.string.common_code_send_hint)
                        countdownView?.start()
                    }

                    override fun onFail(e: Exception?) {
                        countdownView?.start()
                    }
                })
            return
        }
        if (view === forgetView) {
            startActivity(PasswordForgetActivity::class.java)
            return
        }
        if (view === commitView) {
            if (phoneView?.text.toString().length != 11) {
                phoneView?.startAnimation(
                    AnimationUtils.loadAnimation(
                        getContext(),
                        R.anim.shake_anim
                    )
                )
                commitView?.showError(3000)
                toast(R.string.common_phone_input_error)
                return
            }

            // 隐藏软键盘
            hideKeyboard(currentFocus)

            //判断是否同意协议
            if (cb_xieyi?.isChecked == false) {
                MessageDialog.Builder(this)
                    .setTitle("协议提醒")
                    .setMessage("是否同意《用户协议》和《隐私政策》")
                    .setCancel("取消")
                    .setConfirm("同意")
                    .setListener(object : MessageDialog.OnListener {
                        override fun onConfirm(dialog: BaseDialog?) {
                            cb_xieyi?.isChecked = true
                            if (loginTypeCode == 1) {
                                loginByPhoneCode()
                            }else {
                                loginByPwd()
                            }
                        }
                    })
                    .show()
                return
            }

            if (loginTypeCode == 1) {
                loginByPhoneCode()
            }
            return
        }
        if (view === qqView || view === weChatView) {
            val platform: Platform?
            when {
                view === qqView -> {
                    platform = Platform.QQ
                }
                view === weChatView -> {
                    if (packageName.endsWith(".debug")) {
                        toast("当前 buildType 不支持进行微信登录")
                        return
                    }
                    platform = Platform.WECHAT
                }
                else -> {
                    throw IllegalStateException("are you ok?")
                }
            }
            if (cb_xieyi?.isChecked == false) {
                MessageDialog.Builder(this)
                    .setTitle("协议提醒")
                    .setMessage("是否同意《用户协议》和《隐私政策》")
                    .setCancel("取消")
                    .setConfirm("同意")
                    .setListener(object : MessageDialog.OnListener {
                        override fun onConfirm(dialog: BaseDialog?) {
                            cb_xieyi?.isChecked = true
                        }
                    })
                    .show()
                return
            }
            return
        }
        if (view == loginType) {
            if (loginTypeCode == 1) {
                //切换为密码登录
                loginType?.text = "验证码登录"
                loginTypeCode = 2
                login_type_pwd?.visibility = View.VISIBLE
                login_type_code?.visibility = View.GONE

                commitView?.let {
                    InputTextManager.with(this)
                        .addView(phoneView)
                        .addView(et_login_password)
                        .setMain(it)
                        .build()
                }
            } else {
                //切换为验证码登录
                loginType?.text = "密码登录"
                loginTypeCode = 1
                login_type_pwd?.visibility = View.GONE
                login_type_code?.visibility = View.VISIBLE

                commitView?.let {
                    InputTextManager.with(this)
                        .addView(phoneView)
                        .addView(codeView)
                        .setMain(it)
                        .build()
                }
            }
            return
        }
    }

    /**
     * 密码登录
     */
    private fun loginByPwd() {
        commitView?.showProgress()
        EasyHttp.post(this)
            .api(PwdLoginApi().apply {
                phone = phoneView?.text.toString()
                loginPwd = et_login_password?.text.toString()
            })
            .request(object : OnHttpListener<HttpData<LoginApi.TokenResult>>{
                override fun onSucceed(data: HttpData<LoginApi.TokenResult>) {
                    // 更新 Token
                    EasyConfig.getInstance()
                        .addHeader("Authorization", "Bearer ${data.getData()?.getToken()}")
                    //存储到SP中
                    UserManager.saveToken(data.getData())

                    postDelayed({
                        commitView?.showSucceed()
                        postDelayed({
                            // 跳转到首页
                            MainActivity.start(getContext())
                            finish()
                        }, 1000)
                    }, 1000)

                    LiveDataBus.postValue("refreshUserInfo", "")
                }

                override fun onFail(e: java.lang.Exception?) {
                    postDelayed({ commitView?.showError(3000) }, 1000)
                }

            })
    }

    /**
     * 验证码登录
     */
    private fun loginByPhoneCode() {
        EasyHttp.post(this)
            .api(LoginApi().apply {
                setPhone(phoneView?.text.toString())
                code = codeView?.text.toString()
//                    setPassword(passwordView?.text.toString())
            })
            .request(object : OnHttpListener<HttpData<LoginApi.TokenResult?>> {

                override fun onStart(call: Call) {
                    commitView?.showProgress()
                }

                override fun onEnd(call: Call) {}

                override fun onSucceed(data: HttpData<LoginApi.TokenResult?>) {
                    // 更新 Token
                    EasyConfig.getInstance()
                        .addHeader("Authorization", "Bearer ${data.getData()?.getToken()}")
                    //存储到SP中
                    UserManager.saveToken(data.getData())

                    postDelayed({
                        commitView?.showSucceed()
                        postDelayed({
                            // 跳转到首页
                            MainActivity.start(getContext())
                            finish()
                        }, 1000)
                    }, 1000)

                    LiveDataBus.postValue("refreshUserInfo", "")
                }

                override fun onFail(e: Exception?) {
                    postDelayed({ commitView?.showError(3000) }, 1000)
                }
            })
    }

    /**
     * [TextView.OnEditorActionListener]
     */
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // 模拟点击提交按钮
            commitView?.let {
                if (it.isEnabled) {
                    // 模拟点击登录按钮
                    onClick(it)
                    return true
                }
            }
        }
        return false
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig()
            // 指定导航栏背景颜色
            .navigationBarColor(R.color.white)
    }

    /**
     * [KeyboardWatcher.SoftKeyboardStateListener]
     */
    override fun onSoftKeyboardOpened(keyboardHeight: Int) {
        // 执行位移动画
        bodyLayout?.let {
            val objectAnimator: ObjectAnimator = ObjectAnimator.ofFloat(
                it,
                "translationY", 0f, (-(commitView?.height?.toFloat() ?: 0f))
            )
            objectAnimator.duration = animTime.toLong()
            objectAnimator.interpolator = AccelerateDecelerateInterpolator()
            objectAnimator.start()
        }

        // 执行缩小动画
        logoView?.let {
            it.pivotX = it.width / 2f
            it.pivotY = it.height.toFloat()
            val animatorSet = AnimatorSet()
            val scaleX = ObjectAnimator.ofFloat(it, "scaleX", 1f, logoScale)
            val scaleY = ObjectAnimator.ofFloat(it, "scaleY", 1f, logoScale)
            val translationY = ObjectAnimator.ofFloat(
                it, "translationY",
                0f, (-(commitView?.height?.toFloat() ?: 0f))
            )
            animatorSet.play(translationY).with(scaleX).with(scaleY)
            animatorSet.duration = animTime.toLong()
            animatorSet.start()
        }
    }

    override fun onSoftKeyboardClosed() {
        // 执行位移动画
        bodyLayout?.let {
            val objectAnimator: ObjectAnimator = ObjectAnimator.ofFloat(
                it,
                "translationY", it.translationY, 0f
            )
            objectAnimator.duration = animTime.toLong()
            objectAnimator.interpolator = AccelerateDecelerateInterpolator()
            objectAnimator.start()
        }

        // 执行放大动画
        logoView?.let {
            it.pivotX = it.width / 2f
            it.pivotY = it.height.toFloat()

            if (it.translationY == 0f) {
                return
            }

            val animatorSet = AnimatorSet()
            val scaleX: ObjectAnimator = ObjectAnimator.ofFloat(it, "scaleX", logoScale, 1f)
            val scaleY: ObjectAnimator = ObjectAnimator.ofFloat(it, "scaleY", logoScale, 1f)
            val translationY: ObjectAnimator = ObjectAnimator.ofFloat(
                it,
                "translationY", it.translationY, 0f
            )
            animatorSet.play(translationY).with(scaleX).with(scaleY)
            animatorSet.duration = animTime.toLong()
            animatorSet.start()
        }
    }
}