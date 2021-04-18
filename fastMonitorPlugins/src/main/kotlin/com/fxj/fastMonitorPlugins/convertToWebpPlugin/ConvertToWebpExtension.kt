package com.fxj.fastMonitorPlugins.convertToWebpPlugin

open class ConvertToWebpExtension{

    var beEnableWhenDebug:Boolean=false
    var beCheckSize:Boolean=true
    var whiteList:Array<String>?=null
    var bigImageWhiteList:Array<String>?=null
    var cwebpToolsDir:String?=null
    var maxSize:Float= (500 * 1024).toFloat()

    constructor(
        enableWhenDebug:Boolean,
        isCheckSize:Boolean,
        argWhiteList:Array<String>?=null,
        argBigImageWhiteList:Array<String>?=null,
        argCwebpToolsDir:String?=null,
        argMaxSize:Float=(500 * 1024).toFloat()){
        this.beEnableWhenDebug=enableWhenDebug
        this.beCheckSize=isCheckSize
        this.whiteList=argWhiteList
        this.bigImageWhiteList=argBigImageWhiteList
        this.cwebpToolsDir=argCwebpToolsDir
        this.maxSize=argMaxSize
    }

    /*定义一个参数为空的构造函数,避免在使用Extension无法创建一个实例对象*/
    constructor(){

    }
}
