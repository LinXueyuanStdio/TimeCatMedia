package com.xyoye.common_component.config

import com.tencent.mmkv.MMKV
import kotlin.Boolean
import kotlin.Int

object DanmuConfig {
  private val mmkv: MMKV = MMKV.defaultMMKV()!!

  fun putDanmuSize(value: Int) {
    mmkv.putInt("key_danmu_size", value).apply()
  }

  fun getDanmuSize(): Int = mmkv.getInt("key_danmu_size",
      com.xyoye.common_component.config.DanmuConfigTable.danmuSize)

  fun putDanmuSpeed(value: Int) {
    mmkv.putInt("key_danmu_speed", value).apply()
  }

  fun getDanmuSpeed(): Int = mmkv.getInt("key_danmu_speed",
      com.xyoye.common_component.config.DanmuConfigTable.danmuSpeed)

  fun putDanmuAlpha(value: Int) {
    mmkv.putInt("key_danmu_alpha", value).apply()
  }

  fun getDanmuAlpha(): Int = mmkv.getInt("key_danmu_alpha",
      com.xyoye.common_component.config.DanmuConfigTable.danmuAlpha)

  fun putDanmuStoke(value: Int) {
    mmkv.putInt("key_danmu_stoke", value).apply()
  }

  fun getDanmuStoke(): Int = mmkv.getInt("key_danmu_stoke",
      com.xyoye.common_component.config.DanmuConfigTable.danmuStoke)

  fun putShowMobileDanmu(value: Boolean) {
    mmkv.putBoolean("key_show_mobile_danmu", value).apply()
  }

  fun isShowMobileDanmu(): Boolean = mmkv.getBoolean("key_show_mobile_danmu",
      com.xyoye.common_component.config.DanmuConfigTable.showMobileDanmu)

  fun putShowBottomDanmu(value: Boolean) {
    mmkv.putBoolean("key_show_bottom_danmu", value).apply()
  }

  fun isShowBottomDanmu(): Boolean = mmkv.getBoolean("key_show_bottom_danmu",
      com.xyoye.common_component.config.DanmuConfigTable.showBottomDanmu)

  fun putShowTopDanmu(value: Boolean) {
    mmkv.putBoolean("key_show_top_danmu", value).apply()
  }

  fun isShowTopDanmu(): Boolean = mmkv.getBoolean("key_show_top_danmu",
      com.xyoye.common_component.config.DanmuConfigTable.showTopDanmu)

  fun putDanmuMaxCount(value: Int) {
    mmkv.putInt("key_danmu_max_count", value).apply()
  }

  fun getDanmuMaxCount(): Int = mmkv.getInt("key_danmu_max_count",
      com.xyoye.common_component.config.DanmuConfigTable.danmuMaxCount)

  fun putDanmuMaxLine(value: Int) {
    mmkv.putInt("key_danmu_max_line", value).apply()
  }

  fun getDanmuMaxLine(): Int = mmkv.getInt("key_danmu_max_line",
      com.xyoye.common_component.config.DanmuConfigTable.danmuMaxLine)

  fun putCloudDanmuBlock(value: Boolean) {
    mmkv.putBoolean("key_cloud_danmu_block", value).apply()
  }

  fun isCloudDanmuBlock(): Boolean = mmkv.getBoolean("key_cloud_danmu_block",
      com.xyoye.common_component.config.DanmuConfigTable.cloudDanmuBlock)

  fun putAutoLoadLocalDanmu(value: Boolean) {
    mmkv.putBoolean("key_auto_load_local_danmu", value).apply()
  }

  fun isAutoLoadLocalDanmu(): Boolean = mmkv.getBoolean("key_auto_load_local_danmu",
      com.xyoye.common_component.config.DanmuConfigTable.autoLoadLocalDanmu)

  fun putAutoLoadNetworkDanmu(value: Boolean) {
    mmkv.putBoolean("key_auto_load_network_danmu", value).apply()
  }

  fun isAutoLoadNetworkDanmu(): Boolean = mmkv.getBoolean("key_auto_load_network_danmu",
      com.xyoye.common_component.config.DanmuConfigTable.autoLoadNetworkDanmu)

  fun putAutoLoadDanmuNetworkStorage(value: Boolean) {
    mmkv.putBoolean("key_auto_load_danmu_network_storage", value).apply()
  }

  fun isAutoLoadDanmuNetworkStorage(): Boolean =
      mmkv.getBoolean("key_auto_load_danmu_network_storage",
      com.xyoye.common_component.config.DanmuConfigTable.autoLoadDanmuNetworkStorage)

  fun putAutoMatchDanmuNetworkStorage(value: Boolean) {
    mmkv.putBoolean("key_auto_match_danmu_network_storage", value).apply()
  }

  fun isAutoMatchDanmuNetworkStorage(): Boolean =
      mmkv.getBoolean("key_auto_match_danmu_network_storage",
      com.xyoye.common_component.config.DanmuConfigTable.autoMatchDanmuNetworkStorage)

  fun putDanmuUpdateInChoreographer(value: Boolean) {
    mmkv.putBoolean("key_danmu_update_in_choreographer", value).apply()
  }

  fun isDanmuUpdateInChoreographer(): Boolean = mmkv.getBoolean("key_danmu_update_in_choreographer",
      com.xyoye.common_component.config.DanmuConfigTable.danmuUpdateInChoreographer)

  fun putShowDialogBeforePlay(value: Boolean) {
    mmkv.putBoolean("key_show_dialog_before_play", value).apply()
  }

  fun isShowDialogBeforePlay(): Boolean = mmkv.getBoolean("key_show_dialog_before_play",
      com.xyoye.common_component.config.DanmuConfigTable.showDialogBeforePlay)

  fun putAutoLaunchDanmuBeforePlay(value: Boolean) {
    mmkv.putBoolean("key_auto_launch_danmu_before_play", value).apply()
  }

  fun isAutoLaunchDanmuBeforePlay(): Boolean = mmkv.getBoolean("key_auto_launch_danmu_before_play",
      com.xyoye.common_component.config.DanmuConfigTable.autoLaunchDanmuBeforePlay)

  fun putDanmuDebug(value: Boolean) {
    mmkv.putBoolean("key_danmu_debug", value).apply()
  }

  fun isDanmuDebug(): Boolean = mmkv.getBoolean("key_danmu_debug",
      com.xyoye.common_component.config.DanmuConfigTable.danmuDebug)
}
