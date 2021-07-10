package com.xyoye.common_component.utils

import androidx.core.content.pm.PackageInfoCompat
import com.timecat.extend.arms.BaseApplication

/**
 * Created by xyoye on 2020/8/19.
 */

object AppUtils {
    fun getVersionCode(): Long {
        if (SecurityHelper.getInstance().isOfficialApplication) {
            val packageName = BaseApplication.getContext().applicationInfo.packageName
            val packageInfo =
                BaseApplication.getContext().packageManager.getPackageInfo(packageName, 0)
            return PackageInfoCompat.getLongVersionCode(packageInfo)
        }
        return 0L
    }

    fun getVersionName(): String {
        if (SecurityHelper.getInstance().isOfficialApplication) {
            val packageName = BaseApplication.getContext().applicationInfo.packageName
            val packageInfo =
                BaseApplication.getContext().packageManager.getPackageInfo(packageName, 0)
            return packageInfo.versionName
        }
        return "unknown"
    }

    fun checkUpdate() {
    }
}