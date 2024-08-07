package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hjq.shape.layout.ShapeConstraintLayout
import com.yimulin.mobile.app.AppAdapter
import com.yimulin.mobile.R
import com.yimulin.mobile.http.api.GetPayListApi

/**
 * @ClassName: PayListAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/5/29 19:03
 **/
class PayListAdapter(val mContext: Context) : AppAdapter<GetPayListApi.PaylistDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_huiyuan_list) {

        private val layout_root: ShapeConstraintLayout? by lazy { findViewById(R.id.layout_root) }
        private val tv_label: TextView? by lazy { findViewById(R.id.tv_label) }
        private val textView3: TextView? by lazy { findViewById(R.id.textView3) }
        private val textView4: TextView? by lazy { findViewById(R.id.textView4) }
        private val ori_price: TextView? by lazy { findViewById(R.id.ori_price) }

        override fun onBindView(position: Int) {
            val paylistDto = getItem(position)

            tv_label?.text = paylistDto.label
            textView3?.text = paylistDto.title
            textView4?.text = "¥${paylistDto.currentPrice}"
            ori_price?.let {
                it.text = "¥${paylistDto.originalPrice}"
                it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            if (paylistDto.checked) {
                layout_root?.shapeDrawableBuilder
                    ?.setStrokeColor(ContextCompat.getColor(mContext, R.color.common_accent_color))
                    ?.intoBackground()
            } else {
                layout_root?.shapeDrawableBuilder
                    ?.setStrokeColor(ContextCompat.getColor(mContext, R.color.white))
                    ?.intoBackground()
            }
        }
    }
}