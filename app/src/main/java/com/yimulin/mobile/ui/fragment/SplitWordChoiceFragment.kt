package com.yimulin.mobile.ui.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.hjq.base.BaseAdapter
import com.pdlbox.tools.utils.ClipboardUtils
import com.yimulin.mobile.R
import com.yimulin.mobile.ui.adapter.WordsListAdapter
import jackmego.com.jieba_android.JiebaSegmenter
import jackmego.com.jieba_android.RequestCallback

/**
 * @ClassName: SplitWordChoiceFragment
 * @Description: 拆分选词
 * @Author: 常利兵
 * @Date: 2023/5/16 15:01
 **/
class SplitWordChoiceFragment : com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>() {

    private lateinit var wordsListAdapter: WordsListAdapter
    private val input_content: EditText? by lazy { findViewById(R.id.input_content) }
    private val word_list: RecyclerView? by lazy { findViewById(R.id.word_list) }

    private var inputStr: String? = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_split_word_choice
    }

    override fun initView() {
        //初始化分词
        JiebaSegmenter.init(getApplication())

        setOnClickListener(R.id.start_splitting, R.id.copy_text, R.id.clean_content)

        word_list?.also {
            it.layoutManager =
                object : FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.WRAP) {
                    override fun canScrollVertically(): Boolean {
                        return true
                    }
                }
            wordsListAdapter = WordsListAdapter(requireContext())
            wordsListAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(
                    recyclerView: RecyclerView?,
                    itemView: View?,
                    position: Int
                ) {
                    val gravityLabelDto = wordsListAdapter.getItem(position)

                    gravityLabelDto.checked = !gravityLabelDto.checked
                    wordsListAdapter.notifyItemChanged(position)
                }
            })
            it.adapter = wordsListAdapter
        }

        input_content?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                inputStr = s?.toString()
            }
        })
    }

    override fun initData() {

    }

    @com.yimulin.mobile.aop.SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.start_splitting -> {
                if (inputStr.isNullOrEmpty()) {
                    toast("请输入要拆分的内容")
                    return
                }

                JiebaSegmenter.getJiebaSegmenterSingleton()
                    .getDividedStringAsync(inputStr, object : RequestCallback<ArrayList<String>> {
                        override fun onSuccess(result: ArrayList<String>?) {
                            val words = ArrayList<com.yimulin.mobile.http.model.WordDto>()
                            result?.let {
                                it.forEachIndexed { _, s ->
                                    words.add(com.yimulin.mobile.http.model.WordDto(s, false))
                                }
                            }
                            wordsListAdapter.setData(words)
                        }

                        override fun onError(errorMsg: String?) {
                            toast(errorMsg)
                        }
                    })
            }
            R.id.copy_text -> {
                val sp = StringBuilder()
                val wordDtos = wordsListAdapter.getData()
                wordDtos.forEachIndexed { index, wordDto ->
                    if (wordDto.checked) {
                        sp.append(wordDto.name)
                    }
                }
                sp.toString()
                if (sp.isEmpty()) {
                    toast("您还没选中内容哦~")
                    return
                }
                ClipboardUtils.copyText(sp.toString())
                toast("已复制到剪切板")
            }
            R.id.clean_content->{
                input_content?.setText("")
            }
            else -> {}
        }
    }
}