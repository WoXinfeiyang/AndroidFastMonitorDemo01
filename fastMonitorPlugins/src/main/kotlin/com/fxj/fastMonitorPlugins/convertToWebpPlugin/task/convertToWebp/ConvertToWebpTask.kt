package com.fxj.fastMonitorPlugins.convertToWebpPlugin.task.convertToWebp

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

open class ConvertToWebpTask:DefaultTask() {

    companion object{
        val TAG:String= ConvertToWebpTask::class.java.simpleName
    }

    var convertToWebpExtension: ConvertToWebpExtension?=null
    var preConvertFilesTotalLength=0L
    var afterConvertFileTotalLength=0L
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


            for(imageFile in imageFileList){
                preConvertFilesTotalLength+=imageFile.length()
                System.out.println(TAG+": ##doAction##imageFile.absolutePath=${imageFile.absolutePath},imageFile.length=${imageFile.length()}Byte,preCovertFilesTotalLength=${preConvertFilesTotalLength}Byte")
            }

            dispatchCovertTask(imageFileList)

            System.out.println(TAG+": ##doAction##Before convert to webp,all image resSize=${preConvertFilesTotalLength/1024}kb")
            System.out.println(TAG+": ##doAction##After convert to webp,all image resSize=${afterConvertFileTotalLength/1024}kb")
            System.out.println(TAG+": ##doAction##After convert to webp,optimize image resSize=${(preConvertFilesTotalLength-afterConvertFileTotalLength)/1024}kb")
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

    private fun dispatchCovertTask(imageFileList:ArrayList<File>?){
        if(imageFileList==null||imageFileList.size<1){
            return
        }
        val processorsNum=Runtime.getRuntime().availableProcessors()
        if(imageFileList.size<processorsNum){
            for(imageFile in imageFileList){
                convertImage(imageFile)
            }
        }else{
            val taskResultList=ArrayList<Future<Unit>>()
            val pool= Executors.newFixedThreadPool(processorsNum)
            val partNum=imageFileList.size/processorsNum

            for(i in 0 until processorsNum){
                val from=i*partNum
                val to=if(i==processorsNum-1) imageFileList.size-1 else (i+1)*partNum-1

                taskResultList.add(pool.submit(Callable<Unit>{
                    for(index in from..to){
                        convertImage(imageFileList[index])
                    }
                }))
            }

            for(taskResult in taskResultList){
                try{
                    taskResult.get()
                }catch (e:Exception){
                    System.out.println(TAG+": ##dispatchCovertTask##Exception.message=${e.message}")
                }
            }
        }
    }

    private fun convertImage(imageFile:File?){
        val path=imageFile?.path?:null
        ConvertToWebpUtils.instance.convertToWebp(imageFile,convertToWebpExtension?.mCompressionFactor?:null)
        calcNewSize(path)
    }

    private fun calcNewSize(path: String?){
        path?.let {
            if(File(it).exists()){
                afterConvertFileTotalLength+=File(it).length()
            }else{
                val webpFilePath=path.substring(0,it.lastIndexOf(".",))+".webp"
                if(File(webpFilePath).exists()){
                    afterConvertFileTotalLength+=File(webpFilePath).length()
                }else{
                    System.out.println(TAG+": ##calcNewSize##convertToWebp task wrong!")
                }
            }
        }
    }
}