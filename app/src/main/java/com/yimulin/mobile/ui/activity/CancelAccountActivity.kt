package com.yimulin.mobile.ui.activity

import android.view.View
import com.hjq.base.BaseDialog
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeCheckBox
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.R
import com.yimulin.mobile.http.api.CancelAccountApi
import com.yimulin.mobile.http.model.HttpData
import com.yimulin.mobile.manager.ActivityManager
import com.yimulin.mobile.manager.UserManager
import com.yimulin.mobile.ui.dialog.MessageDialog
import java.lang.Exception

/**
 * Created with Android studio
 * Description:注销账号
 * @Author: 常利兵
 * DateTime: 2023-02-10 22:03
 */
class CancelAccountActivity : AppActivity() {

    private val cb_yhxz: ShapeCheckBox? by lazy { findViewById(R.id.cb_yhxz) }

    override fun getLayoutId(): Int {
        return R.layout.activity_cancel_account
    }

    override fun initView() {
        setOnClickListener(R.id.cancel_account_btn)
    }

    override fun initData() {

    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.cancel_account_btn -> {
                if (cb_yhxz?.isChecked == true) {
                    MessageDialog.Builder(this)
                        .setTitle("警告")
                        .setMessage("注销账号可能会造成严重后果,您是否确定要注销?")
                        .setConfirm("确定注销")
                        .setCancel("我再想想")
                        .setListener(object : MessageDialog.OnListener {
                            override fun onConfirm(dialog: BaseDialog?) {
                                toast("注销申请已提交，我们将尽快审核")
//                                cancelAccount()
                                UserManager.cleanToken()
                                startActivity(LoginActivity::class.java)
                                // 进行内存优化，销毁除登录页之外的所有界面
                                ActivityManager.getInstance().finishAllActivities(
                                    LoginActivity::class.java
                                )
                                UserManager.cleanToken()
                            }
                        })
                        .show()
                } else {
                    toast("您还没勾选确认按钮哦~")
                }
            }
            else -> {}
        }
    }

    private fun cancelAccount() {
        EasyHttp.post(this)
            .api(CancelAccountApi())
            .request(object :OnHttpListener<HttpData<Any>>{
                override fun onSucceed(result: HttpData<Any>?) {
                    UserManager.cleanToken()
                    startActivity(LoginActivity::class.java)
                    // 进行内存优化，销毁除登录页之外的所有界面
                    ActivityManager.getInstance().finishAllActivities(
                        LoginActivity::class.java
                    )
                    UserManager.cleanToken()
                }

                override fun onFail(e: Exception?) {
                    toast("提交成功")
                }
            })
    }
}