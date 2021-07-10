package com.xyoye.common_component.config

import com.tencent.mmkv.MMKV
import kotlin.Boolean
import kotlin.Int

object DownloadConfig {
  private val mmkv: MMKV = MMKV.defaultMMKV()!!

  fun putDhtEnable(value: Boolean) {
    mmkv.putBoolean("key_dht_enable", value).apply()
  }

  fun isDhtEnable(): Boolean = mmkv.getBoolean("key_dht_enable",
      com.xyoye.common_component.config.DownloadConfigTable.dhtEnable)

  fun putMaxDownloadTask(value: Int) {
    mmkv.putInt("key_max_download_task", value).apply()
  }

  fun getMaxDownloadTask(): Int = mmkv.getInt("key_max_download_task",
      com.xyoye.common_component.config.DownloadConfigTable.maxDownloadTask)
}
