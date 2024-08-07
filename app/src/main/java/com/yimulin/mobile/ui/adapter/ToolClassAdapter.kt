package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppAdapter
import com.yimulin.mobile.http.model.ToolClassDto

/**
 * @ClassName: ToolClassAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/5/12 14:23
 **/
class ToolClassAdapter(val mContext: Context) : AppAdapter<ToolClassDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_tools_layout) {

        private val tools_num: TextView? by lazy { findViewById(R.id.tools_num) }
        private val class_name: TextView? by lazy { findViewById(R.id.class_name) }
        private val tools_list: RecyclerView? by lazy { findViewById(R.id.tools_list) }

        override fun onBindView(position: Int) {
            val toolClassDto = getItem(position)

            class_name?.text = toolClassDto.className
            tools_num?.text = "${toolClassDto.children.size}个工具"

            tools_list?.also {
                it.layoutManager = FlexboxLayoutManager(mContext)
                val toolsListAdapter = ToolsListAdapter(mContext)
                it.adapter = toolsListAdapter
                toolsListAdapter.setData(toolClassDto.children)
            }
        }
    }
}