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
class ReportBottomPopu(val mContext: Context, val block: (String) -> Unit) :
    BottomPopupView(mContext) {

    private lateinit var adapter: CancelOrderOptionListAdapter
    private lateinit var listOptions: GridView
    private val confirm_view: ShapeTextView? by lazy { findViewById(R.id.confirm_view) }
    private val cancel_view: ShapeTextView? by lazy { findViewById(R.id.cancel_view) }

    override fun getImplLayoutId(): Int {
        return R.layout.popu_report
    }

    private val optionList = arrayListOf(
        "拖欠工资",
        "不缴纳社保",
        "不缴纳五险一金",
        "加班不支付工资",
        "PUA员工",
        "裁员不给补偿",
        "上班迟到要求“乐捐\"",
        "试用期超过法定期限",
        "节假日不正常休息",
        "强制要求加班",
        "不批准员工产假,病假,婚丧假,带薪年休假",
        "通过降低工资的方式迫使员工主动辞职",
        "末位淘汰辞退员工"
    )

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