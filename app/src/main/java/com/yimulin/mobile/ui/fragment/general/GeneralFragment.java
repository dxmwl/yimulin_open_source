package com.yimulin.mobile.ui.fragment.general;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimulin.mobile.R;
import com.yimulin.mobile.ui.base.BaseFragment;
import com.yimulin.mobile.utils.CalculatorUtils;
import com.yimulin.mobile.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *      @author hurley
 *      date : 2018/10/23
 *      github : https://github.com/HurleyJames
 *      desc :
 * </pre>
 */
public class GeneralFragment extends BaseFragment implements GeneralContract.View {
    private static final String TAG = "GeneralFragment";

    @BindView(R2.id.rl_screen)
    RelativeLayout mRlScreen;

    @BindView(R2.id.tv_num)
    TextView mTvNum;

    @BindView(R2.id.ll_keyboard)
    LinearLayout mLlKeyBoard;

    @BindView(R2.id.btn_AC)
    TextView mBtnAC;

    @BindView(R2.id.btn_7)
    TextView mBtn7;

    @BindView(R2.id.btn_4)
    TextView mBtn4;

    @BindView(R2.id.btn_1)
    TextView mBtn1;

    @BindView(R2.id.btn_del)
    RelativeLayout mBtnDel;

    @BindView(R2.id.btn_8)
    TextView mBtn8;

    @BindView(R2.id.btn_5)
    TextView mBtn5;

    @BindView(R2.id.btn_2)
    TextView mBtn2;

    @BindView(R2.id.btn_0)
    TextView mBtn0;

    @BindView(R2.id.btn_divide)
    TextView mBtnDivide;

    @BindView(R2.id.btn_9)
    TextView mBtn9;

    @BindView(R2.id.btn_6)
    TextView mBtn6;

    @BindView(R2.id.btn_3)
    TextView mBtn3;

    @BindView(R2.id.btn_point)
    TextView mBtnPoint;

    @BindView(R2.id.btn_multiply)
    TextView mBtnMultiply;

    @BindView(R2.id.btn_subtract)
    TextView mBtnSub;

    @BindView(R2.id.btn_add)
    TextView mBtnAdd;

    @BindView(R2.id.btn_equal)
    TextView mBtnEqual;

    private StringBuffer mMoney = new StringBuffer("");

    /**
     * 运算结果表达式
     */
    private StringBuffer result = new StringBuffer();

    /**
     * 运算结果
     */
    private String num = "0";

    /**
     * 是否计算过，初始为false
     */
    private boolean isCal = false;

    /**
     * 是否可以删除
     */
    private boolean isDelete = true;

    /**
     * 是否可以在计算结果后添加数字
     */
    private boolean isAddNum = true;


    private static GeneralPresenter mPresenter = GeneralPresenter.newInstance();


    public static GeneralFragment newInstance() {
        return new GeneralFragment();
    }


    @Override
    public int getLayoutId() {
        return R.layout.general_fragment;
    }

    @Override
    public void initViews() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R2.id.btn_0, R2.id.btn_1, R2.id.btn_2, R2.id.btn_3, R2.id.btn_4, R2.id.btn_5, R2.id.btn_6,
            R2.id.btn_7, R2.id.btn_8, R2.id.btn_9, R2.id.btn_point, R2.id.btn_add, R2.id.btn_subtract, R2.id.btn_multiply,
            R2.id.btn_divide, R2.id.btn_percent, R2.id.btn_equal, R2.id.btn_del, R2.id.btn_AC})
    public void onClickNum(View view) {
        Log.e(TAG, mMoney.toString() + "长度：" + mMoney.length());
        //如果已经计算过，则只显示运算结果，不再显示原算术式
        if (isCal) {
            mMoney.delete(0, mMoney.length());
            mMoney.append(num);
            isCal = false;
        }
        int id = view.getId();
        if (id == R.id.btn_AC) {//清空文本内容
            mMoney.delete(0, mMoney.length());
            mMoney = new StringBuffer("");
        } else if (id == R.id.btn_0) {
            char zero = '0';
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            //如果长度大于0且首位不为0或含有.
            if (mMoney.length() > 0) {
                if (mMoney.charAt(0) != zero || mMoney.toString().contains(getString(R.string.point))) {
                    mMoney.append(0);
                }
            }
        } else if (id == R.id.btn_1) {
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            mMoney.append("1");
        } else if (id == R.id.btn_2) {
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            mMoney.append("2");
        } else if (id == R.id.btn_3) {
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            mMoney.append("3");
        } else if (id == R.id.btn_4) {
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            mMoney.append("4");
        } else if (id == R.id.btn_5) {
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            mMoney.append("5");
        } else if (id == R.id.btn_6) {
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            mMoney.append("6");
        } else if (id == R.id.btn_7) {
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            mMoney.append("7");
        } else if (id == R.id.btn_8) {
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            mMoney.append("8");
        } else if (id == R.id.btn_9) {
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            mMoney.append("9");
        } else if (id == R.id.btn_point) {
            if (!isAddNum) {
                mMoney.delete(0, mMoney.length());
                isAddNum = true;
            }
            //如果长度为0，则补0
            if (mMoney.toString().length() == 0) {
                mMoney.append("0");
            }
            //如果最后一个为符号，则补0
            mPresenter.addZeroIfChar(mMoney);
            //判断是否能添加.
            if (mPresenter.isAddPointOrNot(mMoney)) {
                mMoney.append(".");
            }
        } else if (id == R.id.btn_del) {
            if (!isDelete) {
                isDelete = true;
            } else {
                //如果长度不为0，则去掉末尾
                if (mMoney.length() > 0) {
                    mMoney.deleteCharAt(mMoney.length() - 1);
                }
            }
        } else if (id == R.id.btn_add) {
            isAddNum = true;
            //如果刚开始没有输入数字，则补0
            if (mMoney.length() == 0) {
                mMoney.append("0");
            }
            if (mMoney.length() != 0) {
                //如果最后一位是.，则先补0
                mPresenter.addZeroIfPoint(mMoney);
            }
            mPresenter.deleteLastStr(mMoney);
            mMoney.append("+");
        } else if (id == R.id.btn_subtract) {
            isAddNum = true;
            //如果刚开始没有输入数字，则补0
            if (mMoney.length() == 0) {
                mMoney.append("0");
            }
            if (mMoney.length() != 0) {
                //如果最后一位是.，则先补0
                mPresenter.addZeroIfPoint(mMoney);
            }
            //TODO 负号
            mPresenter.deleteLastStr(mMoney);
            mMoney.append("-");
        } else if (id == R.id.btn_multiply) {
            isAddNum = true;
            //如果刚开始没有输入数字，则补0
            if (mMoney.length() == 0) {
                mMoney.append("0");
            }
            if (mMoney.length() != 0) {
                //如果最后一位是.，则先补0
                mPresenter.addZeroIfPoint(mMoney);
            }
            mPresenter.deleteLastStr(mMoney);
            mMoney.append("*");
        } else if (id == R.id.btn_divide) {
            isAddNum = true;
            //如果刚开始没有输入数字，则补0
            if (mMoney.length() == 0) {
                mMoney.append("0");
            }
            if (mMoney.length() != 0) {
                //如果最后一位是.，则先补0
                mPresenter.addZeroIfPoint(mMoney);
            }
            mPresenter.deleteLastStr(mMoney);
            mMoney.append("÷");
        } else if (id == R.id.btn_percent) {
            if (mMoney.length() != 0) {
                String last = mMoney.substring(mMoney.length() - 1);
                //如果最后一位是符号
                if (last.equals(getString(R.string.add))
                        || last.equals(getString(R.string.subtract))
                        || last.equals(getString(R.string.multiply))
                        || last.equals(getString(R.string.divide))
                        || last.equals(getString(R.string.equal))) {
                } else {
                    String strLastNum = mPresenter.getLastNum(mMoney);
                    double lastNumAfterDivide = Double.parseDouble(strLastNum) / 100;
                    String strLastNumAfterDivied = String.valueOf(lastNumAfterDivide);
                    mPresenter.deleteLastNum(mMoney);
                    if (strLastNumAfterDivied.contains(getString(R.string.point))
                            && strLastNumAfterDivied.substring(strLastNumAfterDivied.length() - 1).equals(getString(R.string.zero))) {
                        int lastNum = (int) lastNumAfterDivide;
                        mMoney.append(lastNum);
                    } else {
                        mMoney.append(lastNumAfterDivide);
                    }
                }
            } else {
            }
        } else if (id == R.id.btn_equal) {
            char substract = '-';
            if (mMoney.length() != 0) {
                mPresenter.changeIfLastIsChar(mMoney);
                if (mMoney.charAt(0) == substract) {
                    mMoney.insert(0, "0");
                }
                Log.e(TAG, "长度是" + mMoney.length());
                num = CalculatorUtils.calculate(CalculatorUtils.Suffix(mMoney));
                Log.e(TAG, "运算结果：" + num);
                mMoney.append("=" + num);
                //已经计算过
                isCal = true;
                //计算结果不可删除
                isDelete = false;
                //是否能在计算结果后添加数字
                isAddNum = false;
            }
        }
        //如果已输入，将AC换成C
        if (mMoney.length() > 0) {
            mBtnAC.setText("C");
        }
        if (mMoney.equals(getString(R.string.none)) || mMoney.length() == 0) {
            mBtnAC.setText("AC");
            mTvNum.setText("0");
        } else {
            result = mPresenter.replaceMultiply(mMoney, "*", "x");
            mTvNum.setText(result);
        }
    }

}








