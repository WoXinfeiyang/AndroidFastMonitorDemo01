package com.fxj.fastMonitorPlugins

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.LibraryVariant
import com.fxj.fastMonitorPlugins.convertToWebp.ConvertToWebpExtension
import com.fxj.fastMonitorPlugins.processor.VariantProcessor
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.DefaultDomainObjectSet
import java.util.*


class FastMonitorPlugin :Plugin<Project> {
    companion object{
        val TAG= FastMonitorPlugin::class.java.simpleName
    }
    override fun apply(project: Project) {
        System.out.println(TAG +":##apply##Project=${project}");

        project.extensions.create("convertToWebpExtension", ConvertToWebpExtension::class.java)

        var processores= ServiceLoader.load(VariantProcessor::class.java,javaClass.classLoader).toList()
        System.out.println(TAG +":##apply##processores.size=${processores.size},processores=${processores}")

        if(project.plugins.hasPlugin("com.android.application")){
            var appExtension=project.getExtensions().findByType(AppExtension::class.java)

            System.out.println(TAG +":##apply##project has com.android.application plugin,AppExtension=${appExtension},AppExtension ClassName=${appExtension?.javaClass?.canonicalName}")

            /*模块配置完成之后Project.afterEvaluate调用VariantProcessor.process方法进行处理*/
            project.afterEvaluate{
                var applicationVariants: DomainObjectSet<ApplicationVariant>?=appExtension?.applicationVariants
                System.out.println(TAG +":##apply.afterEvaluate##applicationVariants?.size=${applicationVariants?.size}")

                if (applicationVariants != null&& processores!=null) {
                    for(applicationVariant in applicationVariants ){
                        for(processor in processores){
                            System.out.println(TAG +":##apply.afterEvaluate##ApplicationVariant=${applicationVariant},VariantProcessor=${processor}");
                            processor.process(applicationVariant)
                        }
                    }
                }
            }

        }else if(project.plugins.hasPlugin("com.android.library")){
            var libraryExtension: LibraryExtension? =project.getExtensions().findByType(LibraryExtension::class.java)

            System.out.println(TAG +":##apply##project has com.android.library plugin,LibraryExtension=${libraryExtension},LibraryExtension ClassName=${libraryExtension?.javaClass?.canonicalName}")

            project.afterEvaluate {
                var libraryVariants: DefaultDomainObjectSet<LibraryVariant>?=libraryExtension?.libraryVariants
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
}