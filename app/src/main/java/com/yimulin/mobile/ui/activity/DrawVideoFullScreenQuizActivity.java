//package com.yimulin.mobile.ui.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//
//import com.bytedance.sdk.dp.DPPageState;
//import com.bytedance.sdk.dp.DPSdk;
//import com.bytedance.sdk.dp.DPWidgetDrawParams;
//import com.bytedance.sdk.dp.IDPAdListener;
//import com.bytedance.sdk.dp.IDPDrawListener;
//import com.bytedance.sdk.dp.IDPQuizHandler;
//import com.bytedance.sdk.dp.IDPWidget;
//import com.hjq.toast.ToastUtils;
//import com.yimulin.mobile.R;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @ClassName: DrawVideoFullScreenQuizActivity
// * @Description:
// * @Author: 常利兵
// * @Date: 2024/3/05 0005 11:18
// **/
//public class DrawVideoFullScreenQuizActivity extends AppCompatActivity {
//    private static final String TAG = DrawVideoFullScreenQuizActivity.class.getSimpleName();
//    public static final String CHANNEL_TYPE = "channel_type";
//    public static final String IS_HIDE_FOLLOW = "is_hide_follow";
//    public static final String IS_HIDE_CHANNLE_NAME = "is_hide_channle_name";
//
//    private IDPWidget mIDPWidget;
//    private Fragment mDrawFragment;
//
//    private long mGroupId;
//
//    private long mLastBackTime = -1;
//    private int mChannelType = DPWidgetDrawParams.DRAW_CHANNEL_TYPE_RECOMMEND_FOLLOW;
//    private boolean mIsHideFollow = true;
//    private boolean mIsHideChannelName = true;
//
//    private boolean isInited = false;
//
//    private int mPos;
//
//    public static void start(Activity activity, int channelType, boolean isHideFollow,
//                             boolean isHideChannelName) {
//        Intent intent = new Intent(activity, DrawVideoFullScreenQuizActivity.class);
//        intent.putExtra(CHANNEL_TYPE, channelType);
//        intent.putExtra(IS_HIDE_FOLLOW, isHideFollow);
//        intent.putExtra(IS_HIDE_CHANNLE_NAME, isHideChannelName);
//        activity.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.media_activity_draw_video_full_screen);
//        Intent intent = getIntent();
//        if (intent != null) {
//            mChannelType = intent.getIntExtra(CHANNEL_TYPE, DPWidgetDrawParams.DRAW_CHANNEL_TYPE_RECOMMEND_FOLLOW);
//            mIsHideFollow = intent.getBooleanExtra(IS_HIDE_FOLLOW, false);
//            mIsHideChannelName = intent.getBooleanExtra(IS_HIDE_CHANNLE_NAME, false);
//        }
//
//        if (DPSdk.isStartSuccess()) {
//            init();
//        }
//    }
//
//    private void init() {
//        if (isInited) {
//            return;
//        }
//        //初始化draw组件
//        initDrawWidget();
//        mDrawFragment = mIDPWidget.getFragment();
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.draw_style1_frame, mDrawFragment)
//                .commitAllowingStateLoss();
//        isInited = true;
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("android:support:fragments", null);
//    }
//
//    private void initDrawWidget() {
//        mIDPWidget = DPSdk.factory().createDraw(DPWidgetDrawParams.obtain()
//                .liveAdCodeId("947474066")
//                .liveNativeAdCodeId("947474068")
//                .adOffset(0) //单位 dp，为 0 时可以不设置
//                .quizMode(1)
//                .hideClose(false, null)
//                .listener(new IDPDrawListener() {
//                    @Override
//                    public void onDPRefreshFinish() {
//                        log("onDPRefreshFinish");
//                    }
//
//                    @Override
//                    public void onDPPageChange(int position) {
//                        log("onDPPageChange: " + position);
//                    }
//
//                    @Override
//                    public void onDPPageChange(int position, Map<String, Object> map) {
//                        if (map == null) {
//                            return;
//                        }
//                        mPos = position;
//
//                        log("onDPPageChange: " + position + ", map = " + map);
//                    }
//
//                    @Override
//                    public View onCreateQuizView(ViewGroup container) {
//                        View quizView = LayoutInflater.from(container.getContext()).inflate(R.layout.media_layout_quiz, container, false);
//                        log("onCreateQuizView");
//                        return quizView;
//                    }
//
//                    @Override
//                    public void onQuizBindData(View view, List<String> options, int answer, int lastAnswer, IDPQuizHandler quizHandler, Map<String, Object> feedParamsForCallback) {
//                        super.onQuizBindData(view, options, answer, lastAnswer, quizHandler, feedParamsForCallback);
//                        log("onQuizBindData: options:" + listToString(options) + ", answer = " + answer + ", last answer = " + lastAnswer + ", isCap = " + feedParamsForCallback.get("is_cap"));
//
//                        Button option0 = view.findViewById(R.id.quiz_option0);
//                        Button option1 = view.findViewById(R.id.quiz_option1);
//                        List<Button> optionsList = new ArrayList<>();
//                        optionsList.add(option0);
//                        optionsList.add(option1);
//
//                        TextView cap = view.findViewById(R.id.cap);
//                        cap.setText(((int) feedParamsForCallback.get("is_cap") == 1) ? "#有字幕 " : "#无字幕 ");
//
//                        for (int i = 0; i < optionsList.size(); i++) {
//                            final int index = i;
//                            boolean right = answer == index;
//                            int background = right ? R.drawable.selector_quzi_button_ok : R.drawable.selector_quzi_button_error;
//                            Button button = optionsList.get(index);
//                            button.setText(options.get(index));
//                            button.setBackgroundResource(R.drawable.selector_quzi_button_default);
//                            button.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (right) {
//                                        ToastUtils.show("回答正确");
//                                    } else {
//                                        ToastUtils.show("回答错误");
//                                    }
//                                    for (Button btn : optionsList) {
//                                        btn.setBackgroundResource(R.drawable.selector_quzi_button_default);
//                                    }
//                                    button.setBackgroundResource(background);
//                                    quizHandler.reportResult(index);
//                                    if (mIDPWidget != null) {
//                                        mIDPWidget.setCurrentPage(mPos + 1);
//                                    }
//                                }
//                            });
//                            if (lastAnswer == i) {
//                                button.setBackgroundResource(background);
//                            }
//                        }
//                    }
//
//
//                    @Override
//                    public void onDPVideoPlay(Map<String, Object> map) {
//                        log("onDPVideoPlay map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPVideoOver(Map<String, Object> map) {
//                        log("onDPVideoOver map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPVideoCompletion(Map<String, Object> map) {
//                        log("onDPVideoCompletion map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPClose() {
//                        log("onDPClose");
//                    }
//
//                    @Override
//                    public void onDPReportResult(boolean isSucceed) {
//                        log("onDPReportResult isSucceed = " + isSucceed);
//                    }
//
//                    @Override
//                    public void onDPPageStateChanged(DPPageState pageState) {
//                        log("onDPPageStateChanged pageState = " + pageState.toString());
//                    }
//
//                    @Override
//                    public void onDPReportResult(boolean isSucceed, Map<String, Object> map) {
//                        log("onDPReportResult isSucceed = " + isSucceed + ", map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPRequestStart(@Nullable Map<String, Object> map) {
//                        log("onDPRequestStart");
//                    }
//
//                    @Override
//                    public void onDPRequestSuccess(List<Map<String, Object>> list) {
//                        if (list == null) {
//                            return;
//                        }
//
//                        for (int i = 0; i < list.size(); i++) {
//                            log("onDPRequestSuccess i = " + i + ", map = " + list.get(i).toString());
//                        }
//                    }
//
//                    @Override
//                    public void onDPRequestFail(int code, String msg, @Nullable Map<String,
//                            Object> map) {
//                        if (map == null) {
//                            log("onDPRequestFail code = " + code + ", msg = " + msg);
//                            return;
//                        }
//                        log("onDPRequestFail  code = " + code + ", msg = " + msg + ", map = " + map);
//                    }
//
//                    @Override
//                    public void onDPClickAuthorName(Map<String, Object> map) {
//                        log("onDPClickAuthorName map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPClickAvatar(Map<String, Object> map) {
//                        log("onDPClickAvatar map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPClickComment(Map<String, Object> map) {
//                        log("onDPClickComment map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPClickLike(boolean isLike, Map<String, Object> map) {
//                        log("onDPClickLike isLike = " + isLike + ", map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPVideoPause(Map<String, Object> map) {
//                        log("onDPVideoPause map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPVideoContinue(Map<String, Object> map) {
//                        log("onDPVideoContinue map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPClickShare(Map<String, Object> map) {
//                        log("onDPClickShare map = " + map.toString());
//                    }
//                }).adListener(new IDPAdListener() {
//                    @Override
//                    public void onDPAdRequest(Map<String, Object> map) {
//                        log("onDPAdRequest map =  " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPAdRequestSuccess(Map<String, Object> map) {
//                        log("onDPAdRequestSuccess map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPAdRequestFail(int code, String msg, Map<String, Object> map) {
//                        log("onDPAdRequestFail map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPAdFillFail(Map<String, Object> map) {
//                        log("onDPAdFillFail map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPAdShow(Map<String, Object> map) {
//                        log("onDPAdShow map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPAdPlayStart(Map<String, Object> map) {
//                        log("onDPAdPlayStart map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPAdPlayPause(Map<String, Object> map) {
//                        log("onDPAdPlayPause map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPAdPlayContinue(Map<String, Object> map) {
//                        log("onDPAdPlayContinue map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPAdPlayComplete(Map<String, Object> map) {
//                        log("onDPAdPlayComplete map = " + map.toString());
//                    }
//
//                    @Override
//                    public void onDPAdClicked(Map<String, Object> map) {
//                        log("onDPAdClicked map = " + map.toString());
//                    }
//                }));
//    }
//
//    private static void log(String msg) {
//        Log.d(TAG, String.valueOf(msg));
//    }
//
//    private static String listToString(List<String> list) {
//        if (list == null || list.isEmpty()) {
//            return "";
//        }
//        StringBuilder sb = new StringBuilder();
//        for (String s : list) {
//            sb.append(s);
//            sb.append(",");
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        return sb.toString();
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mIDPWidget != null && !mIDPWidget.canBackPress()) {
//            return;
//        }
//
//        if (mIDPWidget == null) {
//            return;
//        }
//
//        long current = SystemClock.elapsedRealtime();
//        if (current - mLastBackTime > 3000) {
//            mLastBackTime = current;
//            mIDPWidget.backRefresh();
//            return;
//        }
//
//        super.onBackPressed();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (mIDPWidget != null) {
//            mIDPWidget.destroy();
//        }
////        Bus.getInstance().removeListener(function);
//    }
//}
//
