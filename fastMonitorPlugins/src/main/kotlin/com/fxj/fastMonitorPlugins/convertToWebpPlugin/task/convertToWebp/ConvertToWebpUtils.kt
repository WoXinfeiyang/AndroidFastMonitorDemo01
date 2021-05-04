package com.fxj.fastMonitorPlugins.convertToWebpPlugin.task.convertToWebp

import org.gradle.api.GradleException
import org.gradle.api.Project
import java.io.File


class ConvertToWebpUtils {

    companion object{
        val TAG=ConvertToWebpUtils::class.java.simpleName

        val instance:ConvertToWebpUtils by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            ConvertToWebpUtils()
        }
    }

    var cwebpToolPath:String?=null
    lateinit var project:Project
    fun init(pro:Project){
        this.project=pro
    }
    /**
     * 获取cwebp.exe的路径
     * @param cwebpToolsRootDir cwebp工具的根目录
     * */
    fun setCwebpToolPath(cwebpToolsRootDir:String?):String?{
        var result:StringBuilder=java.lang.StringBuilder()
        if(cwebpToolsRootDir!=null&&!cwebpToolsRootDir?.isBlank()){
            result.append(cwebpToolsRootDir)
        }else{
            if(project==null){
                throw GradleException("project must not be Null,You should assignment an Project object to it!")
            }
            result.append(project.rootDir.path)
        }
        val systemPlatform = System.getProperty("os.name")


        if(systemPlatform.contains("Windows")){
            result.append("\\tools\\cwebp\\windows\\cwebp.exe")
        }else{

        }

        System.out.println(TAG+": ##setCwebpToolPath##cwebpToolsDir=${cwebpToolsRootDir}," +
                "result=${result},systemPlatform=${systemPlatform}")

        this.cwebpToolPath=result.toString()
        return this.cwebpToolPath
    }

    fun checkCwebpTool():Boolean{
        var result:Boolean=false

        if(File(cwebpToolPath).exists()){
            result=true
        }

        return result
    }

    fun isImageFile(file:File):Boolean{
        var result:Boolean=false
        val fileName=file.name

        if((fileName.endsWith(".jpg")
                    || fileName.endsWith(".png")
                    || fileName.endsWith(".jpeg"))
            && !fileName.endsWith(".9.png")){
            result=true
        }

        return result
    }

    fun cwebpCmd(param:String?){

        if(this.cwebpToolPath==null||(this.cwebpToolPath?.isBlank()?:true)||param==null||param.isBlank()){
            return
        }

        val systemPlatform = System.getProperty("os.name")
        val cmdStr=this.cwebpToolPath+" ${param}"
        System.out.println(TAG+": ##cwebpCmd##cmdStr=${cmdStr}")
        val process = Runtime.getRuntime().exec(cmdStr)
        process.waitFor()
    }

    fun convertToWebp(imageFile:File?,quality:Long?=null){
        if(imageFile==null||!isImageFile(imageFile)){
            return
        }

        var webpFile:File=File(imageFile.path.substring(0,imageFile.path.lastIndexOf("."))+".webp")

        var cwebpCmdStr="cwebp -q "+(if(quality!=null) quality else 75)+" ${imageFile.path} -o ${webpFile.path}"
        cwebpCmd(cwebpCmdStr)

        System.out.println(TAG+": ##convertToWebp##imageFile.length=${imageFile.length()},webpFile.length=${webpFile.length()}," +
                "imageFile.absolutePath=${imageFile.absolutePath},webpFile.absolutePath=${webpFile.absolutePath}")

        if(webpFile.length()<imageFile.length()){
            if(imageFile.exists()){
                imageFile.delete()
            }
        }else{
            if(webpFile.exists()){
                webpFile.delete()
            }
        }
    }
}