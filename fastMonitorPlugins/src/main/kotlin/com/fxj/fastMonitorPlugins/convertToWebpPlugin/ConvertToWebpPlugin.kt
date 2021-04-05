package com.fxj.fastMonitorPlugins.convertToWebpPlugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

class ConvertToWebpPlugin :Plugin<Project> {
    companion object{
        val TAG= ConvertToWebpPlugin::class.java.simpleName
    }
    override fun apply(target: Project) {
        target.getLogger().log(LogLevel.INFO, TAG,"##apply##Project=${target}")
        System.out.println(TAG +":##apply##Project=${target}");
    }
}