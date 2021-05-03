package com.fxj.fastMonitorPlugins.convertToWebpPlugin.task.convertToWebp

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class ConvertToWebpTask:DefaultTask() {

    companion object{
        val TAG:String= ConvertToWebpTask::class.java.simpleName
    }

    var convertToWebpExtension: ConvertToWebpExtension?=null

    @TaskAction
    fun doAction(){

        convertToWebpExtension=project.extensions.findByType(ConvertToWebpExtension::class.java)?:return

        if(convertToWebpExtension==null||convertToWebpExtension?.open==false){
            return
        }

        val hasAppPlugin:Boolean=project.plugins.hasPlugin("com.android.application")
        var variants=if(hasAppPlugin){
            project.extensions.findByType(AppExtension::class.java)?.applicationVariants
        }else{
            project.extensions.findByType(LibraryExtension::class.java)?.libraryVariants
        }

        System.out.println(
            TAG +": ##doAction##ConvertToWebpExtension=${convertToWebpExtension}," +
                "hasAppPlugin=${hasAppPlugin},variants item type=${if(variants==null||variants?.size<1) null else variants.iterator().next()::class.java.canonicalName}")

    }
}