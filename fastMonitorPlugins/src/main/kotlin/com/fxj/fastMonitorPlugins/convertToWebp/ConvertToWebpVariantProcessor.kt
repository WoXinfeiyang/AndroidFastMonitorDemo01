package com.fxj.fastMonitorPlugins.convertToWebp

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.variant.ApkVariantData
import com.android.build.gradle.tasks.MergeResources
import com.fxj.fastMonitorPlugins.processor.VariantProcessor
import com.google.auto.service.AutoService
import org.gradle.api.Task

@AutoService(VariantProcessor::class)
class ConvertToWebpVariantProcessor: VariantProcessor {
    companion object{
        val TAG= ConvertToWebpVariantProcessor::class.java.simpleName
    }
    override fun process(variant: BaseVariant) {
        System.out.println(TAG +": ##process##variant=${variant},ConvertToWebpVariantProcessor=${this}")
        var variantData: ApkVariantData =(variant as com.android.build.gradle.internal.api.ApplicationVariantImpl).variantData

        var tasks=variantData.getScope().getGlobalScope().getProject().getTasks()

        var convertToWebpTask: Task = tasks.findByName("ConvertToWebpTask")?:tasks.create(
            "ConvertToWebpTask",
            ConvertToWebpTask::class.java)

        var mergeResources: MergeResources= variant.getMergeResourcesProvider().get()
        mergeResources.dependsOn(convertToWebpTask)
    }

}