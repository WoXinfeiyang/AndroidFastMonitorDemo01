package com.fxj.fastMonitorPlugins.convertToWebpPlugin.task.convertToWebp

open class ConvertToWebpExtension{

    var open:Boolean=false
    var beCheckSize:Boolean=true
    var whiteList:Array<String>?=null
    var cwebpToolsDir:String?=null
    var maxSize:Float= (500 * 1024).toFloat()

    constructor(
        beOpen:Boolean,
        isCheckSize:Boolean,
        argWhiteList:Array<String>?=null,
        argCwebpToolsDir:String?=null,
        argMaxSize:Float=(500 * 1024).toFloat()){
        this.open=beOpen
        this.beCheckSize=isCheckSize
        this.whiteList=argWhiteList
        this.cwebpToolsDir=argCwebpToolsDir
        this.maxSize=argMaxSize
    }

    /*定义一个参数为空的构造函数,避免在使用Extension无法创建一个实例对象*/
    constructor(){

    }

    override fun toString(): String {
        return "ConvertToWebpExtension(open=$open, beCheckSize=$beCheckSize, whiteList=${whiteList?.contentToString()}, cwebpToolsDir=$cwebpToolsDir, maxSize=$maxSize)"
    }


}
