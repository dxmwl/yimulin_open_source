package com.yimulin.mobile.ui.fragment

import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import android.widget.EditText
import com.yimulin.mobile.R
import com.yimulin.mobile.ui.activity.ReportActivity
import java.util.*


/**
 * @ClassName: TextToSpeechFragment
 * @Description: 文字转语音
 * @Author: 常利兵
 * @Date: 2023/5/15 22:47
 **/
class TextToSpeechFragment :
    com.yimulin.mobile.app.AppFragment<com.yimulin.mobile.app.AppActivity>(),
    TextToSpeech.OnInitListener {

    private val TAG = "TTS"
    private var mTts: TextToSpeech? = null
    private val input_content: EditText? by lazy { findViewById(R.id.input_content) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_text_to_speech
    }

    override fun initView() {
        setOnClickListener(R.id.btn_switch,R.id.btn_tousu)

        initSpeech()
    }

    private fun initSpeech() {
        //初始化语音。这是一个异步操作。初始化完成后调用oninitListener(第二个参数)。
        mTts = TextToSpeech(requireContext(), this)
    }

    override fun initData() {

    }

    @com.yimulin.mobile.aop.SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btn_tousu->{
                startActivity(ReportActivity::class.java)
            }
            R.id.btn_switch -> {
                val string = input_content?.text?.toString()
                if (string.isNullOrBlank()) {
                    toast("请输入要转换的内容")
                    return
                }
                speechText(string)
            }
            else -> {}
        }
    }

    override fun onDestroy() {
        // 生命周期中结束
        if (mTts != null) {
            mTts?.stop()
            mTts?.shutdown()
        }
        super.onDestroy()
    }

    // 实现TextToSpeech.OnInitListener.
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            mTts?.setPitch(1.0f);//方法用来控制音调
            mTts?.setSpeechRate(1.0f);//用来控制语速
            //设置首选语言为中文,注意，语言可能是不可用的，结果将指示此
            val result: Int? = mTts?.setLanguage(Locale.CHINA)
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                toast("语言数据丢失或不支持该语言")
                //语言数据丢失或不支持该语言。
                Log.e(TAG, "语言数据丢失或不支持该语言")
            } else {
                //检查文档中其他可能的结果代码。
                // 例如，语言可能对区域设置可用，但对指定的国家和变体不可用
                // TTS引擎已成功初始化。
                // 允许用户按下按钮让应用程序再次发言。
                // 设置OnUtteranceCompletedListener来监听TTS的结束事件
                mTts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        showDialog()
                    }

                    override fun onDone(utteranceId: String?) {
                        hideDialog()
                    }

                    override fun onError(utteranceId: String?) {
                        hideDialog()
                        toast("错误:$utteranceId")
                    }

                })
            }
        } else {
            // 初始化失败
            toast("初始化失败")
            Log.e(TAG, "初始化失败")
        }
    }

    private fun speechText(dataStr: String) {
        //TextToSpeech的speak方法有两个重载。
        // 执行朗读的方法
        //speak(CharSequence text,int queueMode,Bundle params,String utteranceId);
        // 将朗读的的声音记录成音频文件
        //synthesizeToFile(CharSequence text,Bundle params,File file,String utteranceId);
        //第二个参数queueMode用于指定发音队列模式，两种模式选择
        //（1）TextToSpeech.QUEUE_FLUSH：该模式下在有新任务时候会清除当前语音任务，执行新的语音任务
        //（2）TextToSpeech.QUEUE_ADD：该模式下会把新的语音任务放到语音任务之后，
        //等前面的语音任务执行完了才会执行新的语音任务
        mTts?.speak(dataStr, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
    }
}