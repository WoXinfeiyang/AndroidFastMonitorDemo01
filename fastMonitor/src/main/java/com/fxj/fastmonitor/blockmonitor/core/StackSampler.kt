package com.fxj.fastmonitor.blockmonitor.core

import android.os.Handler
import android.os.HandlerThread
import android.util.Log

/**
 * 堆栈信息采集类,核心原理通过Thread.getStackTrace()拿到线程堆栈信息
 * */
class StackSampler {

    companion object{
        val TAG=StackSampler::class.java.simpleName
        /**
         * 默认保存的堆栈信息的最大条目*/
        val DEFAULT_MAX_ENTRY_COUNT:Int=100

        /**
         * 默认采样间隔
         * */
        val DEFAULT_SAMPLE_INTERVAL:Int=300

        /**
         * 分隔符,\r--回车,\n--换行;Linux中\n表示回车+换行；
         * Windows中\r\n表示回车+换行;Mac中\r表示回车+换行。
         * */
        val SEPARATOR:String="\r\n"
    }

    var mStackHandlerThread:HandlerThread?=null
    var mStackHandler: Handler?=null

    fun init(){
        Log.d(TAG,"##init##----start---mStackHandlerThread=${this.mStackHandlerThread}")
        /*初始化mStackHandlerThread*/
        if(this.mStackHandlerThread==null){
            this.mStackHandlerThread=object :HandlerThread("BlockMonitor"){
                override fun onLooperPrepared() {
                    Log.d(TAG,"##init HandlerThread.onLooperPrepared##")
                    mStackHandler= Handler(mStackHandlerThread?.looper)
                }
            }
            this.mStackHandlerThread?.start()
        }
        Log.d(TAG,"##init##----end---mStackHandlerThread=${this.mStackHandlerThread}")
    }
}