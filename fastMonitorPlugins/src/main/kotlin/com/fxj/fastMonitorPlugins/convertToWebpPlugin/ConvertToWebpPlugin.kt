package com.fxj.fastMonitorPlugins.convertToWebpPlugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.LibraryVariant
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

        project.extensions.create("convertToWebpExtension",ConvertToWebpExtension::class.java)

        var processores= ServiceLoader.load(VariantProcessor::class.java,javaClass.classLoader).toList()
        System.out.println(TAG+":##apply##processores.size=${processores.size},processores=${processores}")

        if(project.plugins.hasPlugin("com.android.application")){
            var appExtension: AppExtension? =project.getExtensions().findByType(AppExtension::class.java)
            var applicationVariants:Set<ApplicationVariant> ?=appExtension?.applicationVariants

            System.out.println(TAG+":##apply##project has com.android.application plugin,applicationVariants?.size=${applicationVariants?.size},applicationVariants=${applicationVariants}")

            if (applicationVariants != null&& processores!=null) {
                for(applicationVariant in applicationVariants ){
                    for(processor in processores){
                        System.out.println(TAG +":##apply##ApplicationVariant=${applicationVariant},VariantProcessor=${processor}");
                        processor.process(applicationVariant)
                    }
                }
            }
        }else if(project.plugins.hasPlugin("com.android.library")){
            var libraryExtension: LibraryExtension? =project.getExtensions().findByType(LibraryExtension::class.java)
            var libraryVariants:Set<LibraryVariant> ?=libraryExtension?.libraryVariants

            System.out.println(TAG+":##apply##project has com.android.library plugin,libraryVariants?.size=${libraryVariants?.size},libraryVariants=${libraryVariants}")

            if (libraryVariants != null&& processores!=null) {
                for(applicationVariant in libraryVariants ){
                    for(processor in processores){
                        System.out.println(TAG +":##apply##ApplicationVariant=${applicationVariant},VariantProcessor=${processor}");
                        processor.process(applicationVariant)
                    }
                }
            }
        }
    }
}