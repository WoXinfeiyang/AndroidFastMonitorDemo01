package com.fxj.fastmonitor.fpsmonitor

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Choreographer

class FpsMonitor private constructor(){

    var mLastFrameRate:Int=0

    var mMainHandler: Handler =Handler(Looper.getMainLooper())

    var frameRateRunnable=FrameRateRunnable()

    companion object{

        @JvmStatic
        val TAG:String=FpsMonitor::class.java.simpleName

        val instance:FpsMonitor by lazy (mode=LazyThreadSafetyMode.SYNCHRONIZED){
            FpsMonitor()
        }

        /**最大帧率*/
        val MAX_FRAME_RATE:Int=60

        /**FPS采样时间*/
        val FPS_SAMPLING_TIME:Long=10
    }

    fun startFpsMonitor(){
        Choreographer.getInstance().postFrameCallback {frameRateRunnable}
        this.mMainHandler.postDelayed(frameRateRunnable, FPS_SAMPLING_TIME)
    }

    fun stopFpsMonitor(){
        Choreographer.getInstance().removeFrameCallback { frameRateRunnable }
        this.mMainHandler.removeCallbacks(frameRateRunnable)
    }

    inner class FrameRateRunnable: Runnable,Choreographer.FrameCallback {
        var totalFramePerSecond:Int=0
        val TAG:String=FrameRateRunnable::class.java.simpleName

        override fun run() {
            mLastFrameRate=totalFramePerSecond
            if(mLastFrameRate>FpsMonitor.MAX_FRAME_RATE){
                mLastFrameRate= MAX_FRAME_RATE
            }

            /*在此可以进行FPS帧率数据保存操作*/


            mMainHandler.postDelayed(this, FPS_SAMPLING_TIME)/*1s统计一次FPS帧率*/
        }

        override fun doFrame(frameTimeNanos: Long) {
            totalFramePerSecond++
            Log.d(TAG,"##doFrame##frameTimeNanos=${frameTimeNanos},totalFramePerSecond=${totalFramePerSecond}")
            Choreographer.getInstance().postFrameCallback { this}
        }
    }
}