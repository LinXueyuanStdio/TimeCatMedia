package com.xyoye.common_component.config

import com.tencent.mmkv.MMKV
import kotlin.Boolean
import kotlin.Int
import kotlin.String

object SubtitleConfig {
  private val mmkv: MMKV = MMKV.defaultMMKV()!!

  fun putShooterSecret(value: String) {
    mmkv.putString("key_shooter_secret", value).apply()
  }

  fun getShooterSecret(): String? = mmkv.getString("key_shooter_secret",
      com.xyoye.common_component.config.SubtitleConfigTable.shooterSecret)

  fun putTextSize(value: Int) {
    mmkv.putInt("key_text_size", value).apply()
  }

  fun getTextSize(): Int = mmkv.getInt("key_text_size",
      com.xyoye.common_component.config.SubtitleConfigTable.textSize)

  fun putStrokeWidth(value: Int) {
    mmkv.putInt("key_stroke_width", value).apply()
  }

  fun getStrokeWidth(): Int = mmkv.getInt("key_stroke_width",
      com.xyoye.common_component.config.SubtitleConfigTable.strokeWidth)

  fun putTextColor(value: Int) {
    mmkv.putInt("key_text_color", value).apply()
  }

  fun getTextColor(): Int = mmkv.getInt("key_text_color",
      com.xyoye.common_component.config.SubtitleConfigTable.textColor)

  fun putStrokeColor(value: Int) {
    mmkv.putInt("key_stroke_color", value).apply()
  }

  fun getStrokeColor(): Int = mmkv.getInt("key_stroke_color",
      com.xyoye.common_component.config.SubtitleConfigTable.strokeColor)

  fun putAutoLoadLocalSubtitle(value: Boolean) {
    mmkv.putBoolean("key_auto_load_local_subtitle", value).apply()
  }

  fun isAutoLoadLocalSubtitle(): Boolean = mmkv.getBoolean("key_auto_load_local_subtitle",
      com.xyoye.common_component.config.SubtitleConfigTable.autoLoadLocalSubtitle)

  fun putAutoLoadNetworkSubtitle(value: Boolean) {
    mmkv.putBoolean("key_auto_load_network_subtitle", value).apply()
  }

  fun isAutoLoadNetworkSubtitle(): Boolean = mmkv.getBoolean("key_auto_load_network_subtitle",
      com.xyoye.common_component.config.SubtitleConfigTable.autoLoadNetworkSubtitle)

  fun putAutoLoadSubtitleNetworkStorage(value: Boolean) {
    mmkv.putBoolean("key_auto_load_subtitle_network_storage", value).apply()
  }

  fun isAutoLoadSubtitleNetworkStorage(): Boolean =
      mmkv.getBoolean("key_auto_load_subtitle_network_storage",
      com.xyoye.common_component.config.SubtitleConfigTable.autoLoadSubtitleNetworkStorage)
}
