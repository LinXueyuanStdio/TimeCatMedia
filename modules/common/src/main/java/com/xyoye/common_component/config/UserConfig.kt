package com.xyoye.common_component.config

import com.tencent.mmkv.MMKV
import kotlin.Boolean
import kotlin.Int
import kotlin.String

object UserConfig {
  private val mmkv: MMKV = MMKV.defaultMMKV()!!

  fun putUserLoggedIn(value: Boolean) {
    mmkv.putBoolean("key_user_logged_in", value).apply()
  }

  fun isUserLoggedIn(): Boolean = mmkv.getBoolean("key_user_logged_in",
      com.xyoye.common_component.config.UserConfigTable.userLoggedIn)

  fun putUserToken(value: String) {
    mmkv.putString("key_user_token", value).apply()
  }

  fun getUserToken(): String? = mmkv.getString("key_user_token",
      com.xyoye.common_component.config.UserConfigTable.userToken)

  fun putUserCoverIndex(value: Int) {
    mmkv.putInt("key_user_cover_index", value).apply()
  }

  fun getUserCoverIndex(): Int = mmkv.getInt("key_user_cover_index",
      com.xyoye.common_component.config.UserConfigTable.userCoverIndex)
}
