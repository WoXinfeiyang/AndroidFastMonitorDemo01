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

    var mBlockPrinter:BlockPrinter?=null

    fun start(){
        if(mIsRunning){
            Log.e(TAG,"##start##start BlockMonitor when it is running!")
            return
        }

        if(this.mBlockPrinter==null){
            this.mBlockPrinter=BlockPrinter()
        }

        this.mIsRunning=true
        Looper.getMainLooper().setMessageLogging(this.mBlockPrinter)
    }

    fun stop(){
        if(!mIsRunning){
            Log.e(TAG,"##stop## stop BlockMonitor when it has stopped,not running!")
            return
        }
        Looper.getMainLooper().setMessageLogging(null)
        if(this.mBlockPrinter!=null){
            this.mBlockPrinter?.stop()
            this.mBlockPrinter=null
        }

        this.mIsRunning=false
    }
}