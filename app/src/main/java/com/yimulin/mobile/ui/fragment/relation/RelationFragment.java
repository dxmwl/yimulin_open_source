package com.yimulin.mobile.ui.fragment.relation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimulin.mobile.R;
import com.yimulin.mobile.http.model.Relation;
import com.yimulin.mobile.ui.base.BaseFragment;
import com.yimulin.mobile.widget.SingleLineZoomTextView;
import com.yimulin.mobile.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *      @author hurley
 *      date : 2018/11/18 下午10:07
 *      github : https://github.com/HurleyJames
 *      desc :
 * </pre>
 */
public class RelationFragment extends BaseFragment implements RelationContract.View {
    private static final String TAG = "RelationFragment";

    @BindView(R2.id.rl_screen)
    RelativeLayout mRlScreen;

    @BindView(R2.id.tv_call)
    SingleLineZoomTextView mTvCall;

    @BindView(R2.id.tv_relation)
    TextView mTvRelation;

    @BindView(R2.id.ll_keyboard)
    LinearLayout mLlKeyBoard;

    @BindView(R2.id.btn_husband)
    TextView mTvHusband;

    @BindView(R2.id.btn_wife)
    TextView mTvWife;

    @BindView(R2.id.btn_del)
    RelativeLayout mRlDel;

    @BindView(R2.id.btn_AC)
    TextView mTvAC;

    @BindView(R2.id.btn_fathter)
    TextView mTvFather;

    @BindView(R2.id.btn_mother)
    TextView mTvMother;

    @BindView(R2.id.btn_bro1)
    TextView mTvBro1;

    @BindView(R2.id.btn_bro2)
    TextView mTvBro2;

    @BindView(R2.id.btn_sister1)
    TextView mTvSister1;

    @BindView(R2.id.btn_sister2)
    TextView mTvSister2;

    @BindView(R2.id.btn_son)
    TextView mTvSon;

    @BindView(R2.id.btn_daughter)
    TextView mTvDaughter;

    @BindView(R2.id.btn_each_other)
    TextView mTvEachOther;

    @BindView(R2.id.btn_equal)
    TextView mTvEqual;

    /**
     * 每次删除的字数
     */
    private int deleteNum = 4;

    /**
     * 点击次数
     */
    private int count = 0;

    /**
     * 最大点击次数
     */
    private int maxCount = 10;

    private StringBuffer mRelation = new StringBuffer("");

    private RelationPresenter mPresenter = RelationPresenter.newInstance();

    public static RelationFragment newInstance() {
        return new RelationFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.relation_fragment;
    }

    @Override
    public void initViews() {
        mRelation.append(getString(R.string.me));
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

    @OnClick({R2.id.btn_husband, R2.id.btn_wife, R2.id.btn_fathter, R2.id.btn_mother,
            R2.id.btn_bro1, R2.id.btn_bro2, R2.id.btn_sister1, R2.id.btn_sister2,
            R2.id.btn_son, R2.id.btn_daughter, R2.id.btn_AC, R2.id.btn_del})
    public void onClickRelation(View view) {
        String link = getString(R.string.link);
        int id = view.getId();
        if (id == R.id.btn_husband) {
            mRelation.append(link).append(getString(R.string.husband1));
            count++;
        } else if (id == R.id.btn_wife) {
            mRelation.append(link).append(getString(R.string.wife1));
            count++;
        } else if (id == R.id.btn_fathter) {
            mRelation.append(link).append(getString(R.string.father));
            count++;
        } else if (id == R.id.btn_mother) {
            mRelation.append(link).append(getString(R.string.mother));
            count++;
        } else if (id == R.id.btn_bro1) {
            mRelation.append(link).append(getString(R.string.brother1));
            count++;
        } else if (id == R.id.btn_bro2) {
            mRelation.append(link).append(getString(R.string.brother2));
            count++;
        } else if (id == R.id.btn_sister1) {
            mRelation.append(link).append(getString(R.string.sister1));
            count++;
        } else if (id == R.id.btn_sister2) {
            mRelation.append(link).append(getString(R.string.sister2));
            count++;
        } else if (id == R.id.btn_son) {
            mRelation.append(link).append(getString(R.string.son));
            count++;
        } else if (id == R.id.btn_daughter) {
            mRelation.append(link).append(getString(R.string.daughter));
            count++;
        } else if (id == R.id.btn_AC) {//清空文本内容
            count = 0;
            mRelation.delete(0, mRelation.length());
            mRelation.append("我");
            Log.e(TAG, mRelation.toString());

            //清空称呼
            mTvCall.setText("");
        } else if (id == R.id.btn_del) {
            count--;
            //删除
            if (mRelation.length() >= deleteNum) {
                mRelation.delete(mRelation.length() - 3, mRelation.length());
            }
        }
        if (count > maxCount) {
            mTvRelation.setText(getString(R.string.big_count));
        } else {
            mTvRelation.setText(mRelation);
        }
        onClickEqual();
    }

    @OnClick(R2.id.btn_equal)
    public void onClickEqual() {
        final List<Relation.ResultBean.RelationBean> relationList = new ArrayList<>();
        mPresenter.getRelationByJSON(relationList, getContext());
        if (mRelation.toString().equals(getString(R.string.me))) {
            mTvCall.setText(getString(R.string.me));
        } else {
            String call = mPresenter.getRelationship(mRelation, relationList);
            Log.e(TAG, "关系：" + mRelation);
            Log.e(TAG, "最终称呼：" + call);

            mTvCall.setText(call);
        }
    }

}
