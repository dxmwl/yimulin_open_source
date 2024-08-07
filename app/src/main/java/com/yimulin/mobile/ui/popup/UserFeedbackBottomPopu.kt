package com.yimulin.mobile.ui.popup

import android.content.Context
import android.widget.AdapterView
import android.widget.GridView
import com.hjq.shape.view.ShapeTextView
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.ui.adapter.CancelOrderOptionListAdapter

/**
 * @project : YouKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/10
 */
class UserFeedbackBottomPopu(val mContext: Context, val block: (String) -> Unit) : BottomPopupView(mContext) {

    private lateinit var adapter: CancelOrderOptionListAdapter
    private lateinit var listOptions: GridView
    private val confirm_view: ShapeTextView? by lazy { findViewById(R.id.confirm_view) }
    private val cancel_view: ShapeTextView? by lazy { findViewById(R.id.cancel_view) }

    override fun getImplLayoutId(): Int {
        return R.layout.popu_user_feed_back
    }

    private val optionList = arrayListOf("功能问题","bug反馈","界面适配","功能建议","其他问题")

    override fun initPopupContent() {
        super.initPopupContent()
        listOptions = findViewById(R.id.list_of_options)
        listOptions.let {
            adapter = CancelOrderOptionListAdapter(mContext, optionList)
            it.adapter = adapter
            it.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                    adapter.lastPosition = position
                    adapter.notifyDataSetChanged()
                }
        }

        cancel_view?.setOnClickListener {
            dismiss()
        }

        confirm_view?.setOnClickListener {
            dismiss()
            block(optionList[adapter.lastPosition])
        }
    }

    override fun getMaxHeight(): Int {
        return ((XPopupUtils.getAppHeight(context) * .85f).toInt())
    }
}