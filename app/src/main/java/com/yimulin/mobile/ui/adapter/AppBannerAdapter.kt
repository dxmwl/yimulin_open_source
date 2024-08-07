package com.yimulin.mobile.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yimulin.mobile.http.api.AppBannerApi
import com.yimulin.mobile.http.glide.GlideApp
import com.yimulin.mobile.ui.activity.BrowserActivity
import com.yimulin.mobile.ui.dialog.DownloadDialog
import com.youth.banner.adapter.BannerAdapter


/**
 * @project : YunKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/5/31
 */
class AppBannerAdapter(bannerList: ArrayList<AppBannerApi.BannerBean>?) :
    BannerAdapter<AppBannerApi.BannerBean, AppBannerAdapter.BannerViewHolder>(bannerList) {

    inner class BannerViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent?.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: AppBannerApi.BannerBean?,
        position: Int,
        size: Int
    ) {
        holder?.let {
            GlideApp.with(holder.itemView).load(data?.bannerImg).transform(
                MultiTransformation(
                    CenterCrop(), RoundedCorners(ConvertUtils.dp2px(8F))
                )
            ).into(holder.imageView)
        }

        holder?.imageView?.setOnClickListener {
            when (data?.type) {
                1 -> {
                    //下载APP
                    val url = data.params
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    ActivityUtils.startActivity(intent)
                }

                else -> {
                    data?.params?.let { it1 -> BrowserActivity.start(it.context, it1) }
                }
            }
        }
    }
}