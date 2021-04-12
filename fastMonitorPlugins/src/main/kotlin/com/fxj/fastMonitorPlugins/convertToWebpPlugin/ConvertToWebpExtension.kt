package com.fxj.fastMonitorPlugins.convertToWebpPlugin

data class ConvertToWebpExtension(
    var enableWhenDebug:Boolean=false,
    var isCheckSize:Boolean=true,
    var whiteList:Array<String>,
    var bigImageWhiteList:Array<String>,
    var cwebpToolsDir:String="",
    var maxSize:Float= (500 * 1024).toFloat()
){

}
