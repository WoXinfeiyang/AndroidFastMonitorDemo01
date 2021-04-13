package com.fxj.fastMonitorPlugins.convertToWebpPlugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.*
import com.fxj.fastMonitorPlugins.convertToWebpPlugin.processor.VariantProcessor as VariantProcessor

class ConvertToWebpPlugin :Plugin<Project> {
    companion object{
        val TAG= ConvertToWebpPlugin::class.java.simpleName
    }
    override fun apply(project: Project) {
        System.out.println(TAG +":##apply##Project=${project}");

        project.extensions.create("ConvertToWebpExtension",ConvertToWebpExtension::class.java)

        if(project.plugins.hasPlugin("com.android.application")){
            var appExtension: AppExtension? =project.getExtensions().findByType(AppExtension::class.java)
            var applicationVariants:Set<ApplicationVariant> ?=appExtension?.applicationVariants

            var processores= ServiceLoader.load(VariantProcessor::class.java,javaClass.classLoader).toList()

            if (applicationVariants != null&& processores!=null) {
                for(applicationVariant in applicationVariants ){
                    for(processor in processores){
                        System.out.println(TAG +":##apply##ApplicationVariant=${applicationVariant},VariantProcessor=${processor}");
                        processor.process(applicationVariant)
                    }
                }
            }
        }
    }
}