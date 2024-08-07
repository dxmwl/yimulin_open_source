package com.yimulin.mobile.utils

import android.content.Context
import com.luck.picture.lib.engine.UriToFileTransformEngine
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.utils.SandboxTransformUtils

/**
 * @ClassName: MeSandboxFileEngine
 * @Description: 沙盒路径转换
 * @Author: 常利兵
 * @Date: 2023/3/22 17:33
 **/
class MeSandboxFileEngine : UriToFileTransformEngine {
    override fun onUriToFileAsyncTransform(
        context: Context?,
        srcPath: String?,
        mineType: String?,
        call: OnKeyValueResultCallbackListener?
    ) {
        call?.onCallback(
            srcPath,
            SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType)
        )
    }
}