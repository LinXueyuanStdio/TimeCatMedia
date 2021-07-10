package com.xyoye.common_component.config

import com.timecat.extend.arms.BaseApplication

/**
 * Created by xyoye on 2020/7/28.
 */
object DefaultConfig {

    val DEFAULT_CACHE_PATH: String by lazy {
        val context = BaseApplication.getContext()
        val externalFilesDir = context.getExternalFilesDir(null)
        val externalCacheDir = context.externalCacheDir
        return@lazy (when {
            externalFilesDir != null -> {
                externalFilesDir.absolutePath
            }
            externalCacheDir != null -> {
                externalCacheDir.absolutePath
            }
            else -> {
                context.filesDir.absolutePath
            }
        })
    }
}
