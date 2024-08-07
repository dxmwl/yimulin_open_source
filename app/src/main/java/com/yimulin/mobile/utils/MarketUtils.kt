package com.yimulin.mobile.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.yimulin.mobile.ui.dialog.MessageDialog


/**
 * @ClassName: MarketUtils
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/1/03 0003 15:25
 **/
object MarketUtils {
    fun openAppStore(context: Context, packageName: String) {
        val marketMap: MutableMap<String, List<String>> = HashMap()
        marketMap["yyb"] =
            mutableListOf("com.tencent.android.qqdownloader") // 应用宝
        marketMap["huawei"] =
            mutableListOf("com.huawei.appmarket") // 华为
        marketMap["oppo"] =
            mutableListOf("com.oppo.market", "com.heytap.market") // oppo
        marketMap["vivo"] =
            mutableListOf("com.bbk.appstore") // vivo
        marketMap["mi"] =
            mutableListOf("com.xiaomi.market") // 小米
        marketMap["honor"] =
            mutableListOf("com.huawei.appmarket") // 荣耀
        val priorityMarkets: List<String> = ArrayList(
            mutableListOf(
                "huawei", "oppo", "vivo", "mi", "honor", "yyb"
            )
        )
        val packageManager = context.packageManager
        val installedMarkets: MutableList<String> = ArrayList()
        val packages = packageManager.getInstalledPackages(0)
        for (packageInfo in packages) {
            installedMarkets.add(packageInfo.packageName)
        }
        var market: String? = null
        for (priorityMarket in priorityMarkets) {
            if (marketMap.containsKey(priorityMarket)) {
                val channelMarkets = marketMap[priorityMarket]
                if (channelMarkets != null) {
                    for (channelMarket in channelMarkets) {
                        if (installedMarkets.contains(channelMarket)) {
                            market = channelMarket
                            break
                        }
                    }
                }
                if (market != null) {
                    break
                }
            }
        }
        try {
            if (market != null) {
                // 有成功找到，就打开对应的应用商店
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse("market://details?id=$packageName"))
                intent.setPackage(market)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.applicationContext.startActivity(intent)
            } else {
                // 打开应用商店失败 则做其他兼容性处理
                MessageDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("没有找到应用商店，请自行前往应用市场搜索[友你]")
                    .setConfirm("确定")
                    .show()
            }
        } catch (e: Exception) {
            // 打开应用商店失败 则做其他兼容性处理
        }
    }

}