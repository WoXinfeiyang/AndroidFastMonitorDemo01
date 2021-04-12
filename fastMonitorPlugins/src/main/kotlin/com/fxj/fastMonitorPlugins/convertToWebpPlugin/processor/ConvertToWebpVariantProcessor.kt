package com.fxj.fastMonitorPlugins.convertToWebpPlugin.processor

import com.android.build.gradle.api.BaseVariant
import com.google.auto.service.AutoService

@AutoService(VariantProcessor::class)
class ConvertToWebpVariantProcessor:VariantProcessor {
    companion object{
        val TAG=ConvertToWebpVariantProcessor::class.java.simpleName
    }
    override fun process(variant: BaseVariant) {
        System.out.println(TAG+": ##process##variant=${variant}")
    }

}