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
     * true--开始记录,false--结束记录
     * */
    var mPrinterStart:Boolean=false

    var mStartTime:Long=0L

    /**
     *卡顿阈值
     * */
    var mBlockThresholdMillis:Int?=null

    /**主构造函数*/
    init {
        Log.d(TAG,"##init##主构造函数")
    }

    constructor(blockThreasholderMillis:Int= DEFAULT_BLOCK_THRESHOLD_MILLIS){
        this.mBlockThresholdMillis=blockThreasholderMillis
    }

    override fun println(msg: String?) {
        Log.d(TAG,"##BlockPrinter.println##msg=${msg},mBlockThresholdMillis=${this.mBlockThresholdMillis}")

        if(!this.mPrinterStart){
            var mStartTime= System.currentTimeMillis()
            this.mPrinterStart=true
        }else{

            this.mPrinterStart=false
        }
    }
}