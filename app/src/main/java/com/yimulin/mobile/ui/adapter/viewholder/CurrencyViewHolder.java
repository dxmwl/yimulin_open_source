package com.yimulin.mobile.ui.adapter.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yimulin.mobile.R;

/**
 * <pre>
 *      @author hurley
 *      date : 2018/10/30
 *      github : https://github.com/HurleyJames
 * </pre>
 */
public class CurrencyViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout mLlCoinType;
    public TextView mTvCoinTitle;
    public TextView mTvCoinSubTitle;

    public CurrencyViewHolder(View itemView) {
        super(itemView);

        mLlCoinType = itemView.findViewById(R.id.ll_coin_type);
        mTvCoinTitle = itemView.findViewById(R.id.tv_coin_title);
        mTvCoinSubTitle = itemView.findViewById(R.id.tv_coin_subtitle);
    }
}
