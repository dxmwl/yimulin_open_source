package com.yimulin.mobile.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.base.BaseDialog
import com.yimulin.mobile.R
import com.yimulin.mobile.aop.SingleClick
import com.yimulin.mobile.app.AppActivity
import com.yimulin.mobile.db.DbHelper
import com.yimulin.mobile.db.entity.MakeAChoiceEntity
import com.yimulin.mobile.ui.adapter.EditChoiceAdapter
import com.yimulin.mobile.ui.dialog.InputDialog
import kotlin.concurrent.thread

/**
 * @ClassName: EditChoiceActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/10/26 16:46
 **/
class EditChoiceActivity : AppActivity() {

    private var selectAllChoice: List<MakeAChoiceEntity>? = null
    private lateinit var editChoiceAdapter: EditChoiceAdapter
    private val recyclerView2: RecyclerView? by lazy { findViewById(R.id.recyclerView2) }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_choice
    }

    override fun initView() {

        setOnClickListener(R.id.add_choice)

        recyclerView2?.also {
            it.layoutManager = LinearLayoutManager(this)
            editChoiceAdapter = EditChoiceAdapter(this)
            it.adapter = editChoiceAdapter
        }
    }

    override fun initData() {
        thread {
            selectAllChoice = DbHelper.db.makeAChoiceDao().selectAllChoice()
            runOnUiThread {
                editChoiceAdapter.setData(selectAllChoice as MutableList<MakeAChoiceEntity>?)
            }
        }
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.add_choice -> {
                if (selectAllChoice != null && selectAllChoice!!.size > 10) {
                    toast("最多只能添加10个选项哦!")
                    return
                }
                InputDialog.Builder(this)
                    .setTitle("添加选项")
                    .setHint("请输入选项内容(最多5个字)")
                    .setListener(object : InputDialog.OnListener {
                        override fun onConfirm(dialog: BaseDialog?, content: String) {
                            if (content.isBlank() || content.length > 5) {
                                toast("输入内容有误")
                                return
                            }
                            thread {
                                val makeAChoiceEntity =
                                    MakeAChoiceEntity(null, choiceTitle = content)
                                DbHelper.db.makeAChoiceDao().insertAll(makeAChoiceEntity)
                                runOnUiThread {
                                    initData()
                                }
                            }
                        }
                    })
                    .show()

            }
            else -> {}
        }
    }
}