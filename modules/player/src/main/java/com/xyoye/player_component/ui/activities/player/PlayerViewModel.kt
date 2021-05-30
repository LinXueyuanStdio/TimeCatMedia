package com.xyoye.player_component.ui.activities.player

import androidx.lifecycle.viewModelScope
import master.flame.danmaku.danmaku.model.BaseDanmaku
import com.xyoye.common_component.base.BaseViewModel
import com.xyoye.common_component.database.DatabaseManager
import com.xyoye.common_component.network.Retrofit
import com.xyoye.common_component.network.request.httpRequest
import com.xyoye.common_component.utils.DDLog
import com.xyoye.common_component.utils.DanmuUtils
import com.xyoye.common_component.utils.StreamHeaderUtil
import com.xyoye.common_component.utils.getFileName
import com.xyoye.common_component.weight.ToastCenter
import com.xyoye.data_component.bean.PlayParams
import com.xyoye.data_component.bean.SendDanmuBean
import com.xyoye.data_component.data.SendDanmuData
import com.xyoye.data_component.entity.DanmuBlockEntity
import com.xyoye.data_component.entity.PlayHistoryEntity
import com.xyoye.data_component.enums.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*
import java.util.regex.Pattern

class PlayerViewModel : BaseViewModel() {

    val localDanmuBlockLiveData = DatabaseManager.instance.getDanmuBlockDao().getAll(false)
    val cloudDanmuBlockLiveData = DatabaseManager.instance.getDanmuBlockDao().getAll(true)

    fun addPlayHistory(playParams: PlayParams, position: Long, duration: Long) {
        GlobalScope.launch(context = Dispatchers.IO) {
            var videoUrl = playParams.videoPath
            if (playParams.mediaType == MediaType.MAGNET_LINK) {
                videoUrl = getMagnetRealUrl(videoUrl)
            }

            val history = DatabaseManager.instance.getPlayHistoryDao()
                .getPlayHistory(videoUrl, playParams.mediaType)

            val historyEntity = if (history.size == 0) {
                PlayHistoryEntity(
                    0,
                    playParams.videoTitle ?: getFileName(videoUrl),
                    videoUrl,
                    playParams.mediaType,
                    position,
                    duration,
                    Date(),
                    playParams.danmuPath,
                    playParams.episodeId,
                    playParams.subtitlePath,
                    StreamHeaderUtil.header2String(playParams.header),
                    playParams.torrentPath,
                    playParams.torrentFileIndex,
                    playParams.torrentTitle
                )
            } else {
                history[0].apply {
                    videoPosition = position
                    videoDuration = duration
                    playTime = Date()
                    danmuPath = playParams.danmuPath
                    episodeId = playParams.episodeId
                    subtitlePath = playParams.subtitlePath
                    header = StreamHeaderUtil.header2String(playParams.header)
                    torrentPath = playParams.torrentPath
                    torrentFileIndex = playParams.torrentFileIndex
                    torrentTitle = playParams.torrentTitle
                }
            }

            DatabaseManager.instance.getPlayHistoryDao()
                .insert(historyEntity)
        }
    }

    fun bindSource(sourcePath: String, videoPath: String, isSubtitle: Boolean) {
        viewModelScope.launch {
            if (isSubtitle) {
                DatabaseManager.instance.getVideoDao().updateSubtitle(
                    videoPath, sourcePath
                )
            } else {
                DatabaseManager.instance.getVideoDao().updateDanmu(
                    videoPath, sourcePath
                )
            }
        }
    }

    fun addDanmuBlock(keyword: String, isRegex: Boolean) {
        viewModelScope.launch {
            DatabaseManager.instance.getDanmuBlockDao().insert(
                DanmuBlockEntity(0, keyword, isRegex)
            )
        }
    }

    fun removeDanmuBlock(id: Int) {
        viewModelScope.launch {
            DatabaseManager.instance.getDanmuBlockDao().delete(id)
        }
    }

    fun sendDanmu(playParams: PlayParams, sendDanmuBean: SendDanmuBean) {
        if (playParams.episodeId > 0) {
            sendDanmuToServer(sendDanmuBean, playParams.episodeId)
        }
        if (playParams.danmuPath != null) {
            writeDanmuToFile(sendDanmuBean, playParams.danmuPath!!)
        }
    }

    private fun sendDanmuToServer(sendDanmuBean: SendDanmuBean, episodeId: Int) {
        httpRequest<SendDanmuData>(viewModelScope) {

            api {
                val timeDecimal = BigDecimal(sendDanmuBean.position.toDouble() / 1000)
                    .setScale(2, BigDecimal.ROUND_HALF_UP)

                val mode = when {
                    sendDanmuBean.isScroll -> BaseDanmaku.TYPE_SCROLL_RL
                    sendDanmuBean.isTop -> BaseDanmaku.TYPE_FIX_TOP
                    else -> BaseDanmaku.TYPE_FIX_BOTTOM
                }
                val color = sendDanmuBean.color and 0x00FFFFFF

                val params = hashMapOf<String, String>()
                params["time"] = timeDecimal.toString()
                params["mode"] = mode.toString()
                params["color"] = color.toString()
                params["comment"] = sendDanmuBean.text
                Retrofit.service.sendDanmu(episodeId.toString(), params)
            }

            onSuccess {
                DDLog.i("发送弹幕成功")
            }

            onError {
                ToastCenter.showOriginalToast("发送弹幕失败\n x${it.code} ${it.msg}")
            }
        }
    }

    private fun writeDanmuToFile(sendDanmuBean: SendDanmuBean, danmuPath: String) {
        val time = BigDecimal(sendDanmuBean.position.toDouble() / 1000)
            .setScale(2, BigDecimal.ROUND_HALF_UP)
            .toString()

        val mode = when {
            sendDanmuBean.isScroll -> BaseDanmaku.TYPE_SCROLL_RL
            sendDanmuBean.isTop -> BaseDanmaku.TYPE_FIX_TOP
            else -> BaseDanmaku.TYPE_FIX_BOTTOM
        }

        val unixTime = (System.currentTimeMillis() / 1000f).toInt().toString()
        val color = sendDanmuBean.color and 0x00FFFFFF

        val danmuText = "<d p=\"$time,$mode,25,$color,$unixTime,0,0,0\">${sendDanmuBean.text}</d>"

        DanmuUtils.appendDanmu(danmuPath, danmuText)
    }

    /**
     * 获取播放磁链的真实链接
     */
    private fun getMagnetRealUrl(videoUrl: String): String {
        val regex = "http://127.0.0.1:+\\d+/"
        val patterns = Pattern.compile(regex)
        val matcher = patterns.matcher(videoUrl)
        if (matcher.find()) {
            matcher.group(0)?.let {
                return videoUrl.substring(it.length)
            }
        }
        return videoUrl
    }
}