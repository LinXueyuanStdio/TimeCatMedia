package com.xyoye.common_component.config

import com.tencent.mmkv.MMKV
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String

object AppConfig {
  private val mmkv: MMKV = MMKV.defaultMMKV()!!

  fun putShowSplashAnimation(value: Boolean) {
    mmkv.putBoolean("key_show_splash_animation", value).apply()
  }

  fun isShowSplashAnimation(): Boolean = mmkv.getBoolean("key_show_splash_animation",
      com.xyoye.common_component.config.AppConfigTable.showSplashAnimation)

  fun putCachePath(value: String) {
    mmkv.putString("key_cache_path", value).apply()
  }

  fun getCachePath(): String? = mmkv.getString("key_cache_path",
      com.xyoye.common_component.config.AppConfigTable.cachePath)

  fun putShowHiddenFile(value: Boolean) {
    mmkv.putBoolean("key_show_hidden_file", value).apply()
  }

  fun isShowHiddenFile(): Boolean = mmkv.getBoolean("key_show_hidden_file",
      com.xyoye.common_component.config.AppConfigTable.showHiddenFile)

  fun putShowFTPVideoTips(value: Boolean) {
    mmkv.putBoolean("key_show_f_t_p_video_tips", value).apply()
  }

  fun isShowFTPVideoTips(): Boolean = mmkv.getBoolean("key_show_f_t_p_video_tips",
      com.xyoye.common_component.config.AppConfigTable.showFTPVideoTips)

  fun putMagnetResDomain(value: String) {
    mmkv.putString("key_magnet_res_domain", value).apply()
  }

  fun getMagnetResDomain(): String? = mmkv.getString("key_magnet_res_domain",
      com.xyoye.common_component.config.AppConfigTable.magnetResDomain)

  fun putCloudBlockUpdateTime(value: Long) {
    mmkv.putLong("key_cloud_block_update_time", value).apply()
  }

  fun getCloudBlockUpdateTime(): Long = mmkv.getLong("key_cloud_block_update_time",
      com.xyoye.common_component.config.AppConfigTable.cloudBlockUpdateTime)

  fun putDarkMode(value: Int) {
    mmkv.putInt("key_dark_mode", value).apply()
  }

  fun getDarkMode(): Int = mmkv.getInt("key_dark_mode",
      com.xyoye.common_component.config.AppConfigTable.darkMode)

  fun putCommonlyFolder1(value: String) {
    mmkv.putString("key_commonly_folder1", value).apply()
  }

  fun getCommonlyFolder1(): String? = mmkv.getString("key_commonly_folder1",
      com.xyoye.common_component.config.AppConfigTable.commonlyFolder1)

  fun putCommonlyFolder2(value: String) {
    mmkv.putString("key_commonly_folder2", value).apply()
  }

  fun getCommonlyFolder2(): String? = mmkv.getString("key_commonly_folder2",
      com.xyoye.common_component.config.AppConfigTable.commonlyFolder2)

  fun putLastOpenFolder(value: String) {
    mmkv.putString("key_last_open_folder", value).apply()
  }

  fun getLastOpenFolder(): String? = mmkv.getString("key_last_open_folder",
      com.xyoye.common_component.config.AppConfigTable.lastOpenFolder)

  fun putLastOpenFolderEnable(value: Boolean) {
    mmkv.putBoolean("key_last_open_folder_enable", value).apply()
  }

  fun isLastOpenFolderEnable(): Boolean = mmkv.getBoolean("key_last_open_folder_enable",
      com.xyoye.common_component.config.AppConfigTable.lastOpenFolderEnable)
}
