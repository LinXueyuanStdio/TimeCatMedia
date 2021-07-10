package com.xyoye.common_component.config

import com.tencent.mmkv.MMKV
import kotlin.Boolean
import kotlin.Int
import kotlin.String

object PlayerConfig {
  private val mmkv: MMKV = MMKV.defaultMMKV()!!

  fun putAllowOrientationChange(value: Boolean) {
    mmkv.putBoolean("key_allow_orientation_change", value).apply()
  }

  fun isAllowOrientationChange(): Boolean = mmkv.getBoolean("key_allow_orientation_change",
      com.xyoye.common_component.config.PlayerConfigTable.allowOrientationChange)

  fun putUseSurfaceView(value: Boolean) {
    mmkv.putBoolean("key_use_surface_view", value).apply()
  }

  fun isUseSurfaceView(): Boolean = mmkv.getBoolean("key_use_surface_view",
      com.xyoye.common_component.config.PlayerConfigTable.useSurfaceView)

  fun putUseMediaCodeC(value: Boolean) {
    mmkv.putBoolean("key_use_media_code_c", value).apply()
  }

  fun isUseMediaCodeC(): Boolean = mmkv.getBoolean("key_use_media_code_c",
      com.xyoye.common_component.config.PlayerConfigTable.useMediaCodeC)

  fun putUseMediaCodeCH265(value: Boolean) {
    mmkv.putBoolean("key_use_media_code_c_h265", value).apply()
  }

  fun isUseMediaCodeCH265(): Boolean = mmkv.getBoolean("key_use_media_code_c_h265",
      com.xyoye.common_component.config.PlayerConfigTable.useMediaCodeCH265)

  fun putUseOpenSlEs(value: Boolean) {
    mmkv.putBoolean("key_use_open_sl_es", value).apply()
  }

  fun isUseOpenSlEs(): Boolean = mmkv.getBoolean("key_use_open_sl_es",
      com.xyoye.common_component.config.PlayerConfigTable.useOpenSlEs)

  fun putUsePlayerType(value: Int) {
    mmkv.putInt("key_use_player_type", value).apply()
  }

  fun getUsePlayerType(): Int = mmkv.getInt("key_use_player_type",
      com.xyoye.common_component.config.PlayerConfigTable.usePlayerType)

  fun putUsePixelFormat(value: String) {
    mmkv.putString("key_use_pixel_format", value).apply()
  }

  fun getUsePixelFormat(): String? = mmkv.getString("key_use_pixel_format",
      com.xyoye.common_component.config.PlayerConfigTable.usePixelFormat)

  fun putUseVLCPixelFormat(value: String) {
    mmkv.putString("key_use_v_l_c_pixel_format", value).apply()
  }

  fun getUseVLCPixelFormat(): String? = mmkv.getString("key_use_v_l_c_pixel_format",
      com.xyoye.common_component.config.PlayerConfigTable.useVLCPixelFormat)

  fun putUseVLCHWDecoder(value: Int) {
    mmkv.putInt("key_use_v_l_c_h_w_decoder", value).apply()
  }

  fun getUseVLCHWDecoder(): Int = mmkv.getInt("key_use_v_l_c_h_w_decoder",
      com.xyoye.common_component.config.PlayerConfigTable.useVLCHWDecoder)
}
