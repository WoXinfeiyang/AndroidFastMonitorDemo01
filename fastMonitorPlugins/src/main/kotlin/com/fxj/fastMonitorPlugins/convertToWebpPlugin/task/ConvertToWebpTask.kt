package com.fxj.fastMonitorPlugins.convertToWebpPlugin.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class ConvertToWebpTask:DefaultTask() {

    companion object{
        val TAG:String=ConvertToWebpTask::class.java.simpleName
    }

    @TaskAction
    fun doAction(){
        System.out.println(TAG+": ##doAction##")
    }
}