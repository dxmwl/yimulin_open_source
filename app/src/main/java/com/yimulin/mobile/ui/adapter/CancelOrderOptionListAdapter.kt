package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.yimulin.mobile.R

class CancelOrderOptionListAdapter(val mContext: Context, private val optionList: ArrayList<String>) :
    BaseAdapter() {

    var lastPosition = 0 //记录上一次选中的图片位置，默认不选中


    override fun getCount(): Int {
        return optionList.size
    }

    override fun getItem(position: Int): Any {
        return optionList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val optionDto = optionList[position]
        val viewHolder: ViewHolder
        val view: View
        if (convertView == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(mContext).inflate(R.layout.item_cancel_order_option, parent, false)
            viewHolder.layoutItem = view.findViewById(R.id.layout_item)
            viewHolder.payTitle = view.findViewById(R.id.tv_title)
            viewHolder.cbStatus = view.findViewById(R.id.cb_status)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.payTitle.text = optionDto
        if (lastPosition == position) {
            viewHolder.cbStatus.setImageResource(R.drawable.icon_checkbox_on)
        } else {
            viewHolder.cbStatus.setImageResource(R.drawable.icon_checkbox_off)
        }
        return view
    }

    class ViewHolder {
        lateinit var layoutItem: ConstraintLayout
        lateinit var payTitle: TextView
        lateinit var cbStatus: ImageView
    }
}