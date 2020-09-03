package com.fxj.fastmonitor.blockmonitor.core

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 堆栈信息采集类,核心原理通过Thread.getStackTrace()拿到线程堆栈信息
 * */
class StackSampler {

    companion object{
        val TAG:String=StackSampler::class.java.simpleName
        /**
         * 默认保存的堆栈信息的最大条目*/
        const val DEFAULT_MAX_ENTRY_COUNT:Int=100

        /**
         * 默认采样间隔
         * */
        const val DEFAULT_SAMPLE_INTERVAL:Long=300L

        /**
         * 分隔符,\r--回车,\n--换行;Linux中\n表示回车+换行；
         * Windows中\r\n表示回车+换行;Mac中\r表示回车+换行。
         * */
        const val SEPARATOR:String="\r\n"

        private val TIME_FORMATTER = SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.CHINESE)
    }

    var mStackHandlerThread:HandlerThread?=null
    var mStackHandler: Handler?=null

    var mIsRunning:AtomicBoolean= AtomicBoolean(false)

    private var mRunnable:Runnable=object:Runnable{
        override fun run() {
            dumpInfo()
            if(mIsRunning.get()){
                mStackHandler?.postDelayed(this, DEFAULT_SAMPLE_INTERVAL)
            }
        }

    }
    /**
     * 存储栈信息LinkedHashMap,以时间戳作为key,以栈信息作为value。
     * */
    var mStackInfoMap:LinkedHashMap<Long, String> = linkedMapOf<Long, String>()

    /**
     * 上一次存储的栈信息,用于过滤处理
     * */
    var lastStackInfo:String?=null

    private fun dumpInfo(){
        var stackTraceElements= Looper.getMainLooper().thread.getStackTrace()
        var stackTraceElementsStringBuilder:StringBuilder= StringBuilder()
        for(stackTraceElement in stackTraceElements){
            stackTraceElementsStringBuilder.append(stackTraceElement.toString()).append(SEPARATOR)
        }
        Log.d(
            TAG,
            "##StackSampler.dumpInfo##stackTraceElementsStringBuilder=${stackTraceElementsStringBuilder}"
        )
        /*将栈信息写入保存到mStackInfoMap*/
        synchronized(this.mStackInfoMap){
            if(this.mStackInfoMap.size== DEFAULT_MAX_ENTRY_COUNT){
                this.mStackInfoMap.remove(this.mStackInfoMap.keys.iterator().next())
            }
            if(!isDuplicatedStackTrace(stackTraceElementsStringBuilder.toString())){
               this.mStackInfoMap.put(System.currentTimeMillis(),stackTraceElementsStringBuilder.toString())
            }
        }
    }

    /**
     * 判断传入的栈信息strackInfo是否与上次的栈信息相同
     * */
    private fun isDuplicatedStackTrace(strackInfo:String):Boolean{
        if(TextUtils.equals(strackInfo,this.lastStackInfo)){
            return true
        }
        this.lastStackInfo=strackInfo
        return false
    }
    /**
     * 返回在某个统计周期内的栈信息
     * */
    fun getThreadStackEntries(startTime:Long,endTime:Long):ArrayList<String>{
        var result= arrayListOf<String>()
        /*从mStackInfoMap中读数据*/
        synchronized(this.mStackInfoMap){
            var keys = this.mStackInfoMap.keys
            for(entryTime in keys){
                if(entryTime>startTime&&entryTime<endTime){
                    result.add(formateStackInfoEntry(System.currentTimeMillis(),this.mStackInfoMap.get(entryTime)))
                }
            }
        }

        return result
    }

    /**
     * 格式化栈信息
     * */
    private fun formateStackInfoEntry(currentTimeMillis:Long,strackInfoEntry:String?):String{
        return TIME_FORMATTER.format(currentTimeMillis)+ SEPARATOR+strackInfoEntry
    }

    fun init(){
        Log.d(TAG, "##init##----start---mStackHandlerThread=${this.mStackHandlerThread}")
        /*初始化mStackHandlerThread*/
        if(this.mStackHandlerThread==null){
            this.mStackHandlerThread=object :HandlerThread("BlockMonitor"){
                override fun onLooperPrepared() {
                    Log.d(TAG, "##init HandlerThread.onLooperPrepared##")
                    mStackHandler= Handler(mStackHandlerThread?.looper)
                }
            }
            this.mStackHandlerThread?.start()
        }
        Log.d(TAG, "##init##----end---mStackHandlerThread=${this.mStackHandlerThread}")
    }
    /**
     * 开始采样栈信息
     * */
    fun startDump(){
        if(this.mStackHandler==null){
            Log.e(TAG, "##startDump##mStackHandler is null!")
            return
        }
        if(this.mIsRunning.get()){
            return
        }
        this.mIsRunning.set(true)

        this.mStackHandler?.removeCallbacks(this.mRunnable)
        this.mStackHandler?.postDelayed(this.mRunnable, DEFAULT_SAMPLE_INTERVAL)
    }

    /**
     * 停止采样栈信息
     * */
    fun stopDump(){
        this.lastStackInfo=null
        this.mStackHandler?.removeCallbacks(this.mRunnable)
        this.mIsRunning.set(false)
    }

    fun stop(){
        startDump()
        if(this.mStackHandlerThread!=null){
            this.mStackHandlerThread?.quit()
        }
    }
}