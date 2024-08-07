package com.yimulin.mobile.utils

import android.content.Context
import android.net.Uri
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.hjq.toast.ToastUtils
import com.luck.picture.lib.utils.MediaUtils
import com.orhanobut.logger.Logger
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import com.tencent.cos.xml.transfer.COSXMLUploadTask.COSXMLUploadTaskResult
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.qcloud.core.auth.QCloudCredentialProvider
import com.tencent.qcloud.core.auth.SessionCredentialProvider
import com.tencent.qcloud.core.http.HttpRequest
import com.tencent.qcloud.core.http.RequestBodySerializer
import com.yimulin.mobile.BuildConfig
import com.yimulin.mobile.app.AppApplication
import com.yimulin.mobile.manager.UserManager
import com.yimulin.mobile.other.AppConfig
import java.net.URL
import java.util.*


/**
 * @project : YouKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/6
 */
object CosManager : LifecycleOwner {

    private lateinit var cosXmlService: CosXmlService

    //存储桶名称，由bucketname-appid 组成，appid必须填入，可以在COS控制台查看存储桶名称。 https://console.cloud.tencent.com/cos5/bucket
    private var bucket = AppConfig.getBucketName()

    // 存储桶所在地域简称，例如广州地区是 ap-guangzhou
    private const val region = "ap-guangzhou"

    //自定义域名
//    private const val domainName = "https://file.youquan.dxmwl.com/"

    fun init(mContext: Context) {


        try {
            val url = URL(BuildConfig.HOST_URL + "tencent/api/getCosTemporaryKey")

            // 初始化 COS Service，获取实例
            val credentialProvider: QCloudCredentialProvider = SessionCredentialProvider(
                HttpRequest.Builder<String>()
                    .url(url)
                    .method("POST")
                    .body(
                        RequestBodySerializer.string(
                            "text/plain",
                            "this is test"
                        )
                    )
                    .build()
            )
            // 创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
            val serviceConfig = CosXmlServiceConfig.Builder()
                .setRegion(region)
                .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
                .builder()
            cosXmlService =
                CosXmlService(mContext, serviceConfig, credentialProvider)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 上传图片
     */
    fun upLoadFile(filePath: Any, modelType: CosType, block: (Boolean, String) -> Unit) {
        Logger.d("上传地址:${filePath}")
        // 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
        val transferConfig = TransferConfig.Builder().build()
        // 初始化 TransferManager
        val transferManager = TransferManager(
            cosXmlService,
            transferConfig
        )

        val cosPath = "${modelType.code}/${createFileName(filePath)}" //对象在存储桶中的位置标识符，即称对象键
        //若存在初始化分块上传的 UploadId，则赋值对应的 uploadId 值用于续传；否则，赋值 null
        val uploadId: String? = null

        val cosxmlUploadTask = if (filePath is String) {
            transferManager.upload(bucket, cosPath, filePath, uploadId)
        } else if (filePath is Uri) {
            transferManager.upload(bucket, cosPath, filePath, uploadId)
        } else {
            null
        }

        //设置上传进度回调
        //        cosxmlUploadTask.setCosXmlProgressListener { complete, target ->
        //        }
        //设置返回结果回调
        cosxmlUploadTask?.setCosXmlResultListener(object : CosXmlResultListener {
            override fun onSuccess(request: CosXmlRequest, result: CosXmlResult) {
                val uploadResult = result as COSXMLUploadTaskResult
                block(true, uploadResult.accessUrl)
            }

            // 如果您使用 kotlin 语言来调用，请注意回调方法中的异常是可空的，否则不会回调 onFail 方法，即：
            // clientException 的类型为 CosXmlClientException?，serviceException 的类型为 CosXmlServiceException?
            override fun onFail(
                request: CosXmlRequest,
                @Nullable clientException: CosXmlClientException?,
                @Nullable serviceException: CosXmlServiceException?
            ) {
                //如果是临时秘钥过期则重新获取
                init(AppApplication.getApplication())
                block(false, "上传失败")
                if (AppConfig.isDebug()) {
                    ToastUtils.show("图片上传失败:clientException:${clientException?.message},serviceException:${serviceException?.message}")
                } else {
                    ToastUtils.show("该图片存在问题,请换张图片上传哦~")
                }
                Logger.d("图片上传失败:clientException:${clientException?.message},serviceException:${serviceException?.message}")
                if (clientException != null) {
                    clientException.printStackTrace()
                } else {
                    serviceException!!.printStackTrace()
                }
            }
        })
    }

    /**
     * 多图上传
     */
    fun multiFileUpload(
        files: ArrayList<Any>,
        modelType: CosType,
        block: (Boolean, ArrayList<String>) -> Unit
    ) {
        var countNum = 0
        val fileUrlList = ArrayList<String>()
        if (files.isEmpty()) {
            block(true, fileUrlList)
            return
        }
        files.forEach {
            upLoadFile(it, modelType) { status, url ->
                countNum++
                if (status) {
                    fileUrlList.add(url)
                }
                if (countNum == files.size) {
                    if (fileUrlList.size == files.size) {
                        block(true, fileUrlList)
                    } else {
                        block(false, fileUrlList)
                    }
                }
            }
        }
    }

    /**
     * 文件类型
     */
    enum class CosType(val code: Int, desc: String) {

        REPORT_COMPANY(1001, "举报公司"),
        PORTRAIT_ANIME(1002, "人像动漫化"),
        USER_FEEDBACK_IMG(1003, "用户反馈"),
    }

    private fun createFileName(url: Any): String {
        val mediaType = MediaUtils.getMimeTypeFromMediaUrl(url.toString())
        return if (mediaType.startsWith("video")) {
            UserManager.userInfo?.userId + "_" + UUID.randomUUID() + "_android.mp4"
        } else {
            UserManager.userInfo?.userId + "_" + UUID.randomUUID() + "_android.png"
        }
    }

    override fun getLifecycle(): Lifecycle {
        return LifecycleRegistry(this)
    }
}