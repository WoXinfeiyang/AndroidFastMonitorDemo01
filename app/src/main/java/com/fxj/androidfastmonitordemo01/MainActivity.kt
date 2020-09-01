package com.fxj.androidfastmonitordemo01

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.fxj.fastmonitor.fpsmonitor.FpsMonitor

class MainActivity : Activity() {
    companion object{
        val TAG:String= MainActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"##onCreate##")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FpsMonitor.instance.startFpsMonitor()
    }

    override fun onDestroy() {
        Log.d(TAG,"##onDestroy##")
        super.onDestroy()
        FpsMonitor.instance.stopFpsMonitor()
    }
}