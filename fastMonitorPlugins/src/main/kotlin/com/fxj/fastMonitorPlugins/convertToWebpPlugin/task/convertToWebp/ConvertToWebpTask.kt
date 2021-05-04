package com.fxj.fastMonitorPlugins.convertToWebpPlugin.task.convertToWebp

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

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

        ConvertToWebpUtils.instance.init(project)
        ConvertToWebpUtils.instance.setCwebpToolPath(convertToWebpExtension?.cwebpToolsRootDir)

        var checkCwebpToolState=ConvertToWebpUtils.instance.checkCwebpTool()

        System.out.println(TAG+": ##doAction##ConvertToWebpUtils.cwebpToolPath=${ConvertToWebpUtils.instance.cwebpToolPath},checkCwebpToolState=${checkCwebpToolState}")

        if(!checkCwebpToolState){
            return
        }

        variants?.all{variant->
            val resFiles=variant.allRawAndroidResources.files
            val imageFileList=ArrayList<File>()
            val imageFileNameList=ArrayList<String>()

            for(resFile in resFiles){
                traverseResFile(resFile,imageFileList,imageFileNameList)
            }

            System.out.println(TAG+": ##doAction##imageFileList.size=${imageFileList.size},imageFileNameList.size=${imageFileNameList.size}")

            var preCovertFilesTotalLength=0L
            for(imageFile in imageFileList){
                preCovertFilesTotalLength+=imageFile.length()
                System.out.println(TAG+": ##doAction##imageFile.absolutePath=${imageFile.absolutePath},imageFile.length=${imageFile.length()}Byte,preCovertFilesTotalLength=${preCovertFilesTotalLength}Byte")
            }


        }

    }

    private fun traverseResFile(resFile:File?, shouldCovertImageFiles:ArrayList<File>?, shouldCovertImageFileNameList:ArrayList<String>?){
        if(resFile==null||shouldCovertImageFiles==null||shouldCovertImageFileNameList==null){
            return
        }

        if(!resFile.isDirectory){
            filterImageRes(resFile,shouldCovertImageFiles,shouldCovertImageFileNameList)
        }else{
            resFile.listFiles()
            for(file in resFile.listFiles()){
                if(file.isDirectory){
                    traverseResFile(file,shouldCovertImageFiles,shouldCovertImageFileNameList)
                }else{
                    filterImageRes(file,shouldCovertImageFiles,shouldCovertImageFileNameList)
                }
            }
        }
    }

    private fun filterImageRes(file:File, imageFiles:ArrayList<File>?, imageFileNameList:ArrayList<String>?){
        if(file==null||imageFiles==null||imageFileNameList==null){
            return
        }
        if(imageFileNameList?.contains(file.absolutePath)){
            return
        }

        if(ConvertToWebpUtils.instance.isImageFile(file)&&!(convertToWebpExtension?.whiteList?.contains(file.name)?:false)&&!imageFileNameList.contains(file.absolutePath)){
            imageFiles.add(file)
            imageFileNameList.add(file.absolutePath)
        }

    }
}