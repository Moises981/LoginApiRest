package com.example.loginapirest

import android.app.Application
import com.example.loginapirest.common.data.ReqResAPI

class LoginApplication : Application() {
    companion object {
        lateinit var reqResAPI: ReqResAPI
    }

    override fun onCreate() {
        super.onCreate()
        reqResAPI = ReqResAPI.getInstance(this)
    }
}