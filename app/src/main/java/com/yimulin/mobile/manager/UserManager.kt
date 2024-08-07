package com.yimulin.mobile.manager

import com.pdlbox.tools.utils.GsonUtils
import com.pdlbox.tools.utils.SpUtils
import com.yimulin.mobile.http.api.LoginApi
import com.yimulin.mobile.http.api.UserInfoApi

object UserManager {

    //登录状态
    var loginStatus = false

    //Token
    var tokenResult: LoginApi.TokenResult? = null

    //用户信息
    var userInfo: UserInfoApi.UserInfoDto? = null

    private const val SP_USER_DATA = "USER_DATA"
    private const val SP_TOKEN = "TOKEN"
    private const val SP_USER_INFO = "SP_USER_INFO"

    /**
     * 存储Token
     */
    fun saveToken(tokenResult: LoginApi.TokenResult?) {
        loginStatus = tokenResult != null

        if (tokenResult == null) return
        this.tokenResult = tokenResult
        SpUtils.put(SP_USER_DATA, SP_TOKEN, GsonUtils.toJson(tokenResult))
    }

    /**
     * 清空Token
     */
    fun cleanToken() {
        this.tokenResult = null
        this.userInfo = null
        loginStatus = false
        SpUtils.clear(SP_USER_DATA)
        SpUtils.clear(SP_TOKEN)
    }

    fun init() {
        //获取本地token
        val tokenStr = SpUtils.getString(SP_USER_DATA, SP_TOKEN)
        if (tokenStr.isNullOrEmpty().not()) {
            this.tokenResult = GsonUtils.fromJson(tokenStr, LoginApi.TokenResult::class.java)!!
            loginStatus = true
            initUserInfo()
        } else {
            loginStatus = false
        }
    }

    /**
     * 初始化用户信息
     */
    fun initUserInfo(userInfoDto: UserInfoApi.UserInfoDto? = null) {
        if (userInfoDto != null) {
            userInfo = userInfoDto
            saveUserInfoToLocal(userInfoDto)
        } else {
            val userInfoJson = SpUtils.getString(SP_USER_DATA, SP_USER_INFO)
            if (userInfoJson.isNullOrEmpty().not()) {
                userInfo = GsonUtils.fromJson(userInfoJson, UserInfoApi.UserInfoDto::class.java)
            }
        }
    }

    /**
     * 保存用户信息到本地
     */
    private fun saveUserInfoToLocal(userInfoDto: UserInfoApi.UserInfoDto) {
        SpUtils.put(SP_USER_DATA, SP_USER_INFO, GsonUtils.toJson(userInfoDto))
    }
}