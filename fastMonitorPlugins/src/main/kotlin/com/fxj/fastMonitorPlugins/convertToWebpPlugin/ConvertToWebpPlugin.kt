package com.fxj.fastMonitorPlugins.convertToWebpPlugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class ConvertToWebpPlugin :Plugin<Project> {
    companion object{
        val TAG= ConvertToWebpPlugin::class.java.simpleName
    }
    override fun apply(project: Project) {
        System.out.println(TAG +":##apply##Project=${project}");

        project.extensions.create("ConvertToWebpExtension",ConvertToWebpExtension::class.java)

        if(project.plugins.hasPlugin("com.android.application")){
            var appExtension: AppExtension? =project.getExtensions().findByType(AppExtension::class.java)
            appExtension?.applicationVariants

        }
    }
}