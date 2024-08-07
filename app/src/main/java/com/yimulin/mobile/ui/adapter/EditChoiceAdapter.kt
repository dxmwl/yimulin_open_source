package com.yimulin.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.hjq.base.BaseDialog
import com.hjq.toast.ToastUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.app.AppAdapter
import com.yimulin.mobile.db.DbHelper
import com.yimulin.mobile.db.entity.MakeAChoiceEntity
import com.yimulin.mobile.ui.dialog.MessageDialog
import kotlin.concurrent.thread

/**
 * @ClassName: EditChoiceAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/10/26 17:30
 **/
class EditChoiceAdapter(val mContext: Context) : AppAdapter<MakeAChoiceEntity>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_edit_choice) {

        private val tv_title: TextView? by lazy { findViewById(R.id.tv_title) }
        private val btn_del: TextView? by lazy { findViewById(R.id.btn_del) }

        override fun onBindView(position: Int) {

            val item = getItem(position)

            tv_title?.text = item.choiceTitle

            btn_del?.setOnClickListener {
                MessageDialog.Builder(mContext)
                    .setTitle("操作提示")
                    .setMessage("是否删除该选项?")
                    .setConfirm("删除")
                    .setListener(object : MessageDialog.OnListener {
                        override fun onConfirm(dialog: BaseDialog?) {
                            delItem(item)
                        }
                    })
                    .show()
            }
        }

        private fun delItem(item: MakeAChoiceEntity) {
            val count = getCount()
            if (count == 3) {
                ToastUtils.show("至少需要保留3个选项")
                return
            }
            removeItem(item)
            thread {
                val delItem = DbHelper.db.makeAChoiceDao().delItem(item)
                if (delItem > 0) {
                    ToastUtils.show("删除成功")
                } else {
                    ToastUtils.show("删除失败")
                }
            }
        }
    }
}