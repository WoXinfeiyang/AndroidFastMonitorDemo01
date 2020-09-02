package com.fxj.fastmonitor.blockmonitor.core

import android.os.Looper
import android.util.Log
import android.util.Printer

class BlockMonitor private constructor(){


    companion object{
        val TAG=BlockMonitor::class.java.simpleName

        val instance:BlockMonitor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            BlockMonitor()
        }

    }

    /**
     * 卡顿检测是否正在运行
     * */
    private var mIsRunning:Boolean=false


    fun start(){
        if(mIsRunning){
            Log.e(TAG,"##start##start BlockMonitor when it is running!")
            return
        }

        this.mIsRunning=true
        Looper.getMainLooper().setMessageLogging(object : Printer{
            override fun println(msg: String?) {
                Log.d(TAG,"##MessageLogging Printer.println##msg=${msg},currentTimeMillis=${System.currentTimeMillis()}")
            }

        })
    }

    fun stop(){
        if(!mIsRunning){
            Log.e(TAG,"##stop## stop BlockMonitor when it has stopped,not running!")
            return
        }
        Looper.getMainLooper().setMessageLogging(null)

        this.mIsRunning=false
    }
}