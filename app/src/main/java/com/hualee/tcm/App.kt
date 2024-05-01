package com.hualee.tcm

import android.app.Application
import com.hualee.tcm.global.AppSettings

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppSettings.appContext = this
    }

    companion object {
        const val TAG = "LiJie"
    }
}