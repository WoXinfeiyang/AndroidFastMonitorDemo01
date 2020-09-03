package com.fxj.fastmonitor.blockmonitor.core

import android.util.Log
import android.util.Printer

class BlockPrinter:Printer {

    companion object{
        val TAG=BlockPrinter::class.java.simpleName
        /**默认卡顿阈值
         * */
        val DEFAULT_BLOCK_THRESHOLD_MILLIS:Int=200
    }

    /**开始记录Looper.loop开启消息循环方法中输出Logging的标志位，
     * true--开始一次记录,false--结束一次记录
     * */
    var mPrinterStart:Boolean=false

    var mStartTime:Long=0L

    /**
     *卡顿阈值
     * */
    var mBlockThresholdMillis:Int?=null

    var mStackSampler:StackSampler?=null
    /**主构造函数*/
    init {
        Log.d(TAG,"##init##主构造函数")
        this.mStackSampler= StackSampler()
        this.mStackSampler?.init()
    }

    constructor(blockThreasholderMillis:Int= DEFAULT_BLOCK_THRESHOLD_MILLIS){
        this.mBlockThresholdMillis=blockThreasholderMillis
        Log.d(TAG,"##次构造函数##mBlockThresholdMillis=${this.mBlockThresholdMillis}")
    }

    override fun println(msg: String?) {
        Log.d(TAG,"##BlockPrinter.println##msg=${msg},mBlockThresholdMillis=${this.mBlockThresholdMillis}")

        if(!this.mPrinterStart){
            this.mStartTime= System.currentTimeMillis()
            this.mPrinterStart=true
            this.mStackSampler?.startDump()
        }else{
            val endTime:Long=System.currentTimeMillis()
            this.mPrinterStart=false
            if(isBlock(endTime)){
                var stackTraceEntries=this.mStackSampler?.getThreadStackEntries(this.mStartTime,endTime)
                printlnStackTraceEntries(stackTraceEntries)
            }
            this.mStackSampler?.stopDump()
        }
    }

    private fun isBlock(endTime:Long):Boolean{
        return endTime-this.mStartTime> this.mBlockThresholdMillis!!
    }

    private fun printlnStackTraceEntries(stackTraceEntries:ArrayList<String>?){
        if(stackTraceEntries==null||stackTraceEntries.size==0){
            return
        }
        for(stackTraceEntry in stackTraceEntries){
            Log.d(TAG,"##printlnStackTraceEntries##stackTraceEntries=${stackTraceEntry}")
            Log.d(TAG,StackSampler.SEPARATOR)
        }
    }

    fun stop(){
        this.mStackSampler?.stop()
    }
}