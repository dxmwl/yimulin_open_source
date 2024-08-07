package com.yimulin.mobile.ui.fragment.exchange;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimulin.mobile.R;
import com.yimulin.mobile.ui.adapter.CurrencyAdapter;
import com.yimulin.mobile.http.OkHttpEngine;
import com.yimulin.mobile.http.ResultCallback;
import com.yimulin.mobile.http.model.Coin;
import com.yimulin.mobile.http.model.Currency;
import com.yimulin.mobile.ui.base.BaseFragment;
import com.yimulin.mobile.utils.DialogUtils;
import com.yimulin.mobile.utils.ExchangeUtils;
import com.yimulin.mobile.utils.GsonUtils;
import com.yimulin.mobile.utils.TextUtils;
import com.yimulin.mobile.R2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * <pre>
 *      @author hurley
 *      date : 2018/10/23
 *      github : https://github.com/HurleyJames
 *      desc :
 * </pre>
 */
public class ExchangeFragment extends BaseFragment implements ExchangeContract.View {
    private static final String TAG = "ExchangeFragment";

    @BindView(R2.id.ll_country)
    LinearLayout mLlCountry;

    @BindView(R2.id.img_flag1)
    ImageView mIvFlag1;

    @BindView(R2.id.country_title1)
    TextView mTvTitle1;

    @BindView(R2.id.country_money1)
    TextView mTvMoney1;

    @BindView(R2.id.country_coin_type1)
    TextView mTvCoinType1;

    @BindView(R2.id.img_flag2)
    ImageView mIvFlag2;

    @BindView(R2.id.country_title2)
    TextView mTvTitle2;

    @BindView(R2.id.country_money2)
    TextView mTvMoney2;

    @BindView(R2.id.country_coin_type2)
    TextView mTvCoinType2;

    @BindView(R2.id.img_flag3)
    ImageView mIvFlag3;

    @BindView(R2.id.country_title3)
    TextView mTvTitle3;

    @BindView(R2.id.country_money3)
    TextView mTvMoney3;

    @BindView(R2.id.country_coin_type3)
    TextView mTvCoinType3;

    @BindView(R2.id.ll_keyboard)
    LinearLayout mLlKeyBoard;

    @BindView(R2.id.btn_7)
    TextView mTvBtn7;

    @BindView(R2.id.btn_4)
    TextView mTvBtn4;

    @BindView(R2.id.btn_0)
    TextView mTvBtn0;

    @BindView(R2.id.btn_8)
    TextView mTvBtn8;

    @BindView(R2.id.btn_5)
    TextView mTvBtn5;

    @BindView(R2.id.btn_2)
    TextView mTvBtn2;

    @BindView(R2.id.btn_empty)
    TextView mTvBtnEmpty;

    @BindView(R2.id.btn_9)
    TextView mTvBtn9;

    @BindView(R2.id.btn_6)
    TextView mTvBtn6;

    @BindView(R2.id.btn_3)
    TextView mTvBtn3;

    @BindView(R2.id.btn_point)
    TextView mTvBtnPoint;

    @BindView(R2.id.btn_AC)
    TextView mTvBtnAC;

    @BindView(R2.id.btn_del)
    RelativeLayout mRlBtnDel;

    @BindView(R2.id.divide_vertical1)
    View mViewDivide1;

    @BindView(R2.id.ll_column1)
    LinearLayout mLlColumn1;

    @BindView(R2.id.rl_country1)
    RelativeLayout mRlCountry1;

    @BindView(R2.id.rl_country2)
    RelativeLayout mRlCountry2;

    @BindView(R2.id.rl_country3)
    RelativeLayout mRlCountry3;

    private StringBuffer mMoney = new StringBuffer("");

    /**
     * 换算汇率路径
     */
    private static String path = "http://op.juhe.cn/onebox/exchange/currency?key=";

    /**
     * 聚合数据key
     */
    private static String key = "e179779db8e8afee7e459cc5af3f7b5b";

    /**
     * 基础url
     */
    private String baseUrl = path + key;
    /**
     * 货币1换算成货币2的url
     */
    private String url1to2;
    /**
     * 货币1换算成货币3的url
     */
    private String url1to3;
    /**
     * 货币2换算成货币1的url
     */
    private String url2to1;
    /**
     * 货币2换算成货币3的url
     */
    private String url2to3;
    /**
     * 货币3换算成货币1的url
     */
    private String url3to1;
    /**
     * 货币3换算成货币2的url
     */
    private String url3to2;

    /**
     * 是否可添加
     */
    private boolean isAdd = true;

    /**
     * 点击光标所在的item，默认为1
     */
    private int mClickItem = 1;

    private int mItem1 = 1;
    private int mItem2 = 2;
    private int mItem3 = 3;

    /**
     * 最多保留的小数位数
     */
    private int maxDecimal = 4;

    /**
     * 货币名称
     */
    private View mCoin;
    private TextView mCoinName;

    private CurrencyAdapter mAdapter;

    private List<String> mCurrencyList;

    private ExchangePresenter mPresenter = ExchangePresenter.newInstance();

    public ExchangeFragment mFragment;

    public static ExchangeFragment newInstance() {
        return new ExchangeFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.exchange_rate_fragment;
    }

    @Override
    public void initViews() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, root);

        //设置第一列的竖直分割线的高度为3/4
        final ViewGroup.LayoutParams layoutParams = mViewDivide1.getLayoutParams();
        mViewDivide1.post(new Runnable() {
            @Override
            public void run() {
                layoutParams.height = mViewDivide1.getHeight() * 3 / 4;
                mViewDivide1.setLayoutParams(layoutParams);
                Log.d(TAG, String.valueOf(mViewDivide1.getHeight()));
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R2.id.btn_0, R2.id.btn_1, R2.id.btn_2, R2.id.btn_3, R2.id.btn_4, R2.id.btn_5, R2.id.btn_6,
            R2.id.btn_7, R2.id.btn_8, R2.id.btn_9, R2.id.btn_point, R2.id.btn_AC, R2.id.btn_del, R2.id.btn_empty,
            R2.id.rl_country1, R2.id.rl_country2, R2.id.rl_country3})
    public void onClickNum(View view) {
        char zero = '0';

        Log.e(TAG, mMoney.toString() + "长度：" + mMoney.length());
        int viewId = view.getId();
        if (viewId == R.id.btn_AC) {//清空文本内容
            mMoney.delete(0, mMoney.length());
            mMoney = new StringBuffer("");
            //AC清空后又可以重新输入数据
            isAdd = true;
        } else if (viewId == R.id.rl_country1) {
            mClickItem = 1;
            Log.e(TAG, "测试光标所在的item" + mClickItem);
        } else if (viewId == R.id.rl_country2) {
            mClickItem = 2;
            Log.e(TAG, "测试光标所在的item" + mClickItem);
        } else if (viewId == R.id.rl_country3) {
            mClickItem = 3;
            Log.e(TAG, "测试光标所在的item" + mClickItem);
        }
        if (isAdd) {
            int id = view.getId();
            if (id == R.id.btn_0) {//如果长度大于0且首位不为0
                if (mMoney.length() > 0 && mMoney.charAt(0) != zero) {
                    mMoney.append("0");
                }
                //点击0
            } else if (id == R.id.btn_empty) {//如果长度大于0且首位不为0
                if (mMoney.length() > 0 && mMoney.charAt(0) != zero) {
                    mMoney.append("0");
                }
            } else if (id == R.id.btn_1) {
                mMoney.append("1");
            } else if (id == R.id.btn_2) {
                mMoney.append("2");
            } else if (id == R.id.btn_3) {
                mMoney.append("3");
            } else if (id == R.id.btn_4) {
                mMoney.append("4");
            } else if (id == R.id.btn_5) {
                mMoney.append("5");
            } else if (id == R.id.btn_6) {
                mMoney.append("6");
            } else if (id == R.id.btn_7) {
                mMoney.append("7");
            } else if (id == R.id.btn_8) {
                mMoney.append("8");
            } else if (id == R.id.btn_9) {
                mMoney.append("9");
            } else if (id == R.id.btn_point) {//如果不包含.，则可以添加.；如果长度不为0，则可以添加.
                if (!mMoney.toString().contains(getString(R.string.point))) {
                    if (mMoney.toString().length() == 0) {
                        mMoney.append("0");
                    }
                    mMoney.append(".");
                }
            } else if (id == R.id.btn_del) {//如果长度不为0，则去掉末尾
                if (mMoney.length() > 0) {
                    mMoney.deleteCharAt(mMoney.length() - 1);
                }
            }
        }

        //显示金额
        if (mMoney.equals(getString(R.string.none)) || mMoney.length() == 0) {
            mPresenter.onClickAC(mTvMoney1, mTvMoney2, mTvMoney3);
        } else {
            if (mClickItem == mItem1) {
                double money1 = Double.parseDouble(mMoney.toString());
                //设置money1
                if (TextUtils.howManyDecimal(money1) <= maxDecimal) {
                    mTvMoney1.setText(mMoney);
                } else {
                    //超过4位小数，不可再添加数字
                    isAdd = false;
                }
            } else if (mClickItem == mItem2) {
                double money2 = Double.parseDouble(mMoney.toString());
                //设置money2
                if (TextUtils.howManyDecimal(money2) <= maxDecimal) {
                    mTvMoney2.setText(mMoney);
                } else {
                    //超过4位小数，不可再添加数字
                    isAdd = false;
                }
            } else if (mClickItem == mItem3) {
                double money3 = Double.parseDouble(mMoney.toString());
                //设置money3
                if (TextUtils.howManyDecimal(money3) <= maxDecimal) {
                    mTvMoney3.setText(mMoney);
                } else {
                    //超过4位小数，不可再添加数字
                    isAdd = false;
                }
            }

            getCurrency(mTvTitle1, mTvTitle2, mTvTitle3, mTvMoney1, mTvMoney2, mTvMoney3, mClickItem);
        }
    }

    /**
     * 获取汇率
     *
     * @param tv1
     * @param tv2
     * @param tv3
     * @param tvMoney1
     * @param tvMoney2
     * @param tvMoney3
     * @param onClickItem
     */
    public void getCurrency(TextView tv1, TextView tv2, TextView tv3,
                            final TextView tvMoney1, final TextView tvMoney2, final TextView tvMoney3,
                            final int onClickItem) {
        String coin1 = tv1.getText().toString();
        String coin2 = tv2.getText().toString();
        String coin3 = tv3.getText().toString();
        url1to2 = ExchangeUtils.getUrl(baseUrl, coin1, coin2);
        url1to3 = ExchangeUtils.getUrl(baseUrl, coin1, coin3);
        url2to1 = ExchangeUtils.getUrl(baseUrl, coin2, coin1);
        url2to3 = ExchangeUtils.getUrl(baseUrl, coin2, coin3);
        url3to1 = ExchangeUtils.getUrl(baseUrl, coin3, coin1);
        url3to2 = ExchangeUtils.getUrl(baseUrl, coin3, coin2);


        if (onClickItem == 1) {
            Log.e(TAG, "获取链接url1to2：" + url1to2);
            Log.e(TAG, "获取链接url1to3：" + url1to3);

            //获取1到2
            OkHttpEngine.getInstance().getAsynHttp(url1to2, new ResultCallback() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(String str) throws IOException {
                    mCurrencyList = new ArrayList<>();
                    //获取请求成功
                    Log.e(TAG, str);
                    //把json转变成对象
                    final Currency currency = GsonUtils.getObject(str, Currency.class);
                    if (!mPresenter.isResultRight(currency)) {
                        if (getActivity() != null) {
                            DialogUtils.showAlertDialog(getActivity(), null, currency.getReason());
                        }
                        mPresenter.onClickAC(tvMoney1, tvMoney2, tvMoney3);
                    } else {
                        for (int i = 0; i < currency.getResult().size(); i++) {
                            mCurrencyList.add(currency.getResult().get(i).getResult());
                            Log.e(TAG, mCurrencyList.get(i));
                        }

                        double money1 = Double.parseDouble(tvMoney1.getText().toString());
                        double money2 = ExchangeUtils.showExchangedMoney(money1,
                                Double.parseDouble(mCurrencyList.get(0)));

                        setMoney(tvMoney1, tvMoney2, tvMoney3, money2, 2, onClickItem);
                    }
                }
            });

            //获取1到3
            OkHttpEngine.getInstance().getAsynHttp(url1to3, new ResultCallback() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(String str) throws IOException {
                    mCurrencyList = new ArrayList<>();
                    //获取请求成功
                    Log.e(TAG, str);
                    //把json转变成对象
                    final Currency currency = GsonUtils.getObject(str, Currency.class);
                    if (!mPresenter.isResultRight(currency)) {
                        if (getActivity() != null) {
                        }
                        mPresenter.onClickAC(tvMoney1, tvMoney2, tvMoney3);
                    } else {
                        for (int i = 0; i < currency.getResult().size(); i++) {
                            mCurrencyList.add(currency.getResult().get(i).getResult());
                            Log.e(TAG, mCurrencyList.get(i));
                        }

                        double money1 = Double.parseDouble(tvMoney1.getText().toString());
                        double money3 = ExchangeUtils.showExchangedMoney(money1,
                                Double.parseDouble(mCurrencyList.get(0)));
                        setMoney(tvMoney1, tvMoney2, tvMoney3, money3, 3, onClickItem);
                    }

                }
            });
        } else if (onClickItem == mItem2) {
            Log.e(TAG, "获取链接url2to1：" + url2to1);
            Log.e(TAG, "获取链接url2to3：" + url2to3);

            //获取2到1
            OkHttpEngine.getInstance().getAsynHttp(url2to1, new ResultCallback() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(String str) throws IOException {
                    mCurrencyList = new ArrayList<>();
                    //获取请求成功
                    Log.e(TAG, str);
                    //把json转变成对象
                    final Currency currency = GsonUtils.getObject(str, Currency.class);
                    if (!mPresenter.isResultRight(currency)) {
                        if (getActivity() != null) {
                            DialogUtils.showAlertDialog(getActivity(), null, currency.getReason());
                        }
                        mPresenter.onClickAC(tvMoney1, tvMoney2, tvMoney3);
                    } else {
                        for (int i = 0; i < currency.getResult().size(); i++) {
                            mCurrencyList.add(currency.getResult().get(i).getResult());
                            Log.e(TAG, mCurrencyList.get(i));
                        }

                        double money2 = Double.parseDouble(tvMoney2.getText().toString());
                        double money1 = ExchangeUtils.showExchangedMoney(money2,
                                Double.parseDouble(mCurrencyList.get(0)));

                        setMoney(tvMoney1, tvMoney2, tvMoney3, money1, 1, onClickItem);
                    }
                }
            });

            //获取2到3
            OkHttpEngine.getInstance().getAsynHttp(url2to3, new ResultCallback() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(String str) throws IOException {
                    mCurrencyList = new ArrayList<>();
                    //获取请求成功
                    Log.e(TAG, str);
                    //把json转变成对象
                    final Currency currency = GsonUtils.getObject(str, Currency.class);
                    if (!mPresenter.isResultRight(currency)) {
                        if (getActivity() != null) {
                        }
                        mPresenter.onClickAC(tvMoney1, tvMoney2, tvMoney3);
                    } else {
                        for (int i = 0; i < currency.getResult().size(); i++) {
                            mCurrencyList.add(currency.getResult().get(i).getResult());
                            Log.e(TAG, mCurrencyList.get(i));
                        }

                        double money2 = Double.parseDouble(tvMoney2.getText().toString());
                        double money3 = ExchangeUtils.showExchangedMoney(money2,
                                Double.parseDouble(mCurrencyList.get(0)));
                        setMoney(tvMoney1, tvMoney2, tvMoney3, money3, 3, onClickItem);
                    }
                }
            });
        } else if (onClickItem == mItem3) {
            Log.e(TAG, "获取链接url3to1：" + url3to1);
            Log.e(TAG, "获取链接url3to2：" + url3to2);

            //获取3到1
            OkHttpEngine.getInstance().getAsynHttp(url3to1, new ResultCallback() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(String str) throws IOException {
                    mCurrencyList = new ArrayList<>();
                    //获取请求成功
                    Log.e(TAG, str);
                    //把json转变成对象
                    final Currency currency = GsonUtils.getObject(str, Currency.class);
                    if (!mPresenter.isResultRight(currency)) {
                        if (getActivity() != null) {
                            DialogUtils.showAlertDialog(getActivity(), null, currency.getReason());
                        }
                        mPresenter.onClickAC(tvMoney1, tvMoney2, tvMoney3);
                    } else {
                        for (int i = 0; i < currency.getResult().size(); i++) {
                            mCurrencyList.add(currency.getResult().get(i).getResult());
                            Log.e(TAG, mCurrencyList.get(i));
                        }

                        double money3 = Double.parseDouble(tvMoney3.getText().toString());
                        double money1 = ExchangeUtils.showExchangedMoney(money3,
                                Double.parseDouble(mCurrencyList.get(0)));

                        setMoney(tvMoney1, tvMoney2, tvMoney3, money1, 1, onClickItem);
                    }
                }
            });

            //获取3到2
            OkHttpEngine.getInstance().getAsynHttp(url3to2, new ResultCallback() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(String str) throws IOException {
                    mCurrencyList = new ArrayList<>();
                    //获取请求成功
                    Log.e(TAG, str);
                    //把json转变成对象
                    final Currency currency = GsonUtils.getObject(str, Currency.class);
                    if (!mPresenter.isResultRight(currency)) {
                        if (getActivity() != null) {
                        }
                        mPresenter.onClickAC(tvMoney1, tvMoney2, tvMoney3);
                    } else {
                        for (int i = 0; i < currency.getResult().size(); i++) {
                            mCurrencyList.add(currency.getResult().get(i).getResult());
                            Log.e(TAG, mCurrencyList.get(i));
                        }

                        double money3 = Double.parseDouble(tvMoney3.getText().toString());
                        double money2 = ExchangeUtils.showExchangedMoney(money3,
                                Double.parseDouble(mCurrencyList.get(0)));
                        setMoney(tvMoney1, tvMoney2, tvMoney3, money2, 2, onClickItem);
                    }
                }
            });
        }

    }

    /**
     * 货币显示的规则
     *
     * @param tvMoney1
     * @param tvMoney2
     * @param tvMoney3
     * @param moneyTo
     * @param to
     * @param onClickItem
     */
    private void setMoney(TextView tvMoney1, TextView tvMoney2, TextView tvMoney3, double moneyTo, int to, int onClickItem) {
        if (onClickItem == 1) {
            if (to == mItem2) {
                if (TextUtils.howManyDecimal(moneyTo) <= maxDecimal) {
                    if (moneyTo == 0.0) {
                        tvMoney2.setText("0");
                    } else {
                        tvMoney2.setText(String.valueOf(moneyTo));
                    }
                } else {
                    //保留4位小数
                    tvMoney2.setText(String.valueOf(TextUtils.saveFourDecimal(moneyTo)));
                }
            } else if (to == mItem3) {
                if (TextUtils.howManyDecimal(moneyTo) <= maxDecimal) {
                    if (moneyTo == 0.0) {
                        tvMoney3.setText("0");
                    } else {
                        tvMoney3.setText(String.valueOf(moneyTo));
                    }
                } else {
                    //保留4位小数
                    tvMoney3.setText(String.valueOf(TextUtils.saveFourDecimal(moneyTo)));
                }
            }
        } else if (onClickItem == mItem2) {
            if (to == 1) {
                if (TextUtils.howManyDecimal(moneyTo) <= maxDecimal) {
                    if (moneyTo == 0.0) {
                        tvMoney1.setText("0");
                    } else {
                        tvMoney1.setText(String.valueOf(moneyTo));
                    }
                } else {
                    //保留4位小数
                    tvMoney1.setText(String.valueOf(TextUtils.saveFourDecimal(moneyTo)));
                }
            } else if (to == mItem3) {
                if (TextUtils.howManyDecimal(moneyTo) <= maxDecimal) {
                    if (moneyTo == 0.0) {
                        tvMoney3.setText("0");
                    } else {
                        tvMoney3.setText(String.valueOf(moneyTo));
                    }
                } else {
                    //保留4位小数
                    tvMoney3.setText(String.valueOf(TextUtils.saveFourDecimal(moneyTo)));
                }
            }
        } else if (onClickItem == mItem3) {
            if (to == 1) {
                if (TextUtils.howManyDecimal(moneyTo) <= maxDecimal) {
                    if (moneyTo == 0.0) {
                        tvMoney1.setText("0");
                    } else {
                        tvMoney1.setText(String.valueOf(moneyTo));
                    }
                } else {
                    //保留4位小数
                    tvMoney1.setText(String.valueOf(TextUtils.saveFourDecimal(moneyTo)));
                }
            } else if (to == mItem2) {
                if (TextUtils.howManyDecimal(moneyTo) <= maxDecimal) {
                    if (moneyTo == 0.0) {
                        tvMoney2.setText("0");
                    } else {
                        tvMoney2.setText(String.valueOf(moneyTo));
                    }
                } else {
                    //保留4位小数
                    tvMoney2.setText(String.valueOf(TextUtils.saveFourDecimal(moneyTo)));
                }
            }
        }

    }


    /**
     * 点击国家弹出选择货币种类Dialog
     *
     * @param textView
     */
    @OnClick({R2.id.country_title1, R2.id.country_title2, R2.id.country_title3})
    public void onClickCountry(TextView textView) {
        final List<Coin.ResultBean.ListBean> coinList = new ArrayList<>();
        ExchangePresenter.newInstance().getCoinFromLocal(coinList, getContext());
        int id = textView.getId();
        if (id == R.id.country_title1) {
            mCoin = getActivity().getWindow().getDecorView().findViewById(R.id.country_coin1);
            mCoinName = mCoin.findViewById(R.id.country_coin_type1);
        } else if (id == R.id.country_title2) {
            mCoin = getActivity().getWindow().getDecorView().findViewById(R.id.country_coin2);
            mCoinName = mCoin.findViewById(R.id.country_coin_type2);
        } else if (id == R.id.country_title3) {
            mCoin = getActivity().getWindow().getDecorView().findViewById(R.id.country_coin3);
            mCoinName = mCoin.findViewById(R.id.country_coin_type3);
        }

        DialogUtils.showCoinDialog(getActivity(), getString(R.string.currency_dialog_title),
                textView, mCoinName, mAdapter, coinList,
                mTvTitle1, mTvTitle2, mTvTitle3, mTvMoney1, mTvMoney2, mTvMoney3, mClickItem);

    }

}