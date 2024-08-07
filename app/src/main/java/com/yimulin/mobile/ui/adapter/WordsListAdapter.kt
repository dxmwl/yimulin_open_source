package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hjq.shape.layout.ShapeLinearLayout
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppAdapter
import com.yimulin.mobile.http.model.WordDto

/**
 * @ClassName: WordsListAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/5/16 15:16
 **/
class WordsListAdapter(val mContext:Context): AppAdapter<WordDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder:AppViewHolder(R.layout.item_words){

        private val label_bg: ShapeLinearLayout?by lazy { findViewById(R.id.label_bg) }
        private val tv_label: TextView?by lazy { findViewById(R.id.tv_label) }

        override fun onBindView(position: Int) {

            val gravityLabelDto = getItem(position)

            tv_label?.text = gravityLabelDto.name

            if (gravityLabelDto.checked){
                label_bg?.shapeDrawableBuilder
                    ?.setSolidColor(ContextCompat.getColor(mContext,R.color.label_bg_checked))
                    ?.intoBackground()
                tv_label?.setTextColor(ContextCompat.getColor(mContext,R.color.label_text_color_checked))
            }else{
                label_bg?.shapeDrawableBuilder
                    ?.setSolidColor(ContextCompat.getColor(mContext,R.color.label_bg_no_checked))
                    ?.setStrokeColor(ContextCompat.getColor(mContext,R.color.label_stroke_no_checked))
                    ?.setStrokeWidth(1)
                    ?.intoBackground()
                tv_label?.setTextColor(ContextCompat.getColor(mContext,R.color.label_text_color_no_checked))
            }
        }
    }
}