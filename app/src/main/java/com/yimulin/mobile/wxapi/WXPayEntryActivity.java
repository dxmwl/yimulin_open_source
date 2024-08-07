package com.yimulin.mobile.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.hjq.toast.ToastUtils;

import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yimulin.mobile.http.model.WxPayMsg;
import com.hjq.base.livebus.LiveDataBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.d("进入微信支付回调页");
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp("wx167f9827a1280219");
        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d("进入微信支付回调页:onNewIntent:" + intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        Logger.d("支付回调结果:" + resp);
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "支付成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消支付";
                break;
            default:
                result = "支付失败";
                break;
        }
        LiveDataBus.postValue("SendWechatPayInfo",new WxPayMsg(resp));
        ToastUtils.show(result);
        finish();
    }
}