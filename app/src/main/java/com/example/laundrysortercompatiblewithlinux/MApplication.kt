package com.example.laundrysortercompatiblewithlinux

import android.app.Application
import android.content.Context

lateinit var mEntireApplicationContext: Context
class MApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        mEntireApplicationContext = this
    }
}