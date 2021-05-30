package com.xyoye.common_component.base.app

import android.app.Application
import android.content.Context
import android.os.Handler
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.bugly.Bugly
import com.xyoye.common_component.BuildConfig
import com.xyoye.common_component.utils.SecurityHelper

/**
 * Created by xyoye on 2020/4/13.
 */

open class BaseApplication : Application() {
    companion object {

        private var APPLICATION_CONTEXT: Application? = null
        private var mMainHandler: Handler? = null

        fun getAppContext(): Context {
            return APPLICATION_CONTEXT!!
        }

        fun getMainHandler(): Handler {
            return mMainHandler!!
        }
    }

    override fun onCreate() {
        super.onCreate()

        APPLICATION_CONTEXT = this
        mMainHandler = Handler(getAppContext().mainLooper)

        if (BuildConfig.IS_DEBUG_MODE) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        Bugly.init(
            applicationContext,
            SecurityHelper.getInstance().buglyId,
            BuildConfig.IS_DEBUG_MODE
        )
    }
}