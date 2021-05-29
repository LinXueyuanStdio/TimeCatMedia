package com.xyoye.player.controller.danmu

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.xyoye.common_component.config.DanmuConfig
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDisplayer.DANMAKU_STYLE_STROKEN
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.ui.widget.DanmakuView
import com.xyoye.danmaku.BiliDanmakuParser
import com.xyoye.danmaku.BiliDanmakuLoader
import com.xyoye.common_component.weight.ToastCenter
import com.xyoye.danmaku.filter.KeywordFilter
import com.xyoye.danmaku.filter.RegexFilter
import com.xyoye.data_component.bean.SendDanmuBean
import com.xyoye.data_component.entity.DanmuBlockEntity
import com.xyoye.data_component.enums.PlayState
import com.xyoye.player.controller.video.InterControllerView
import com.xyoye.player.wrapper.ControlWrapper
import com.xyoye.player.info.PlayerInitializer
import java.io.File
import kotlin.math.max


/**
 * Created by xyoye on 2020/11/17.
 */

class DanmuView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DanmakuView(context, attrs, defStyleAttr), InterControllerView {
    companion object {
        private const val DANMU_MAX_TEXT_SIZE = 2f
        private const val DANMU_MAX_TEXT_ALPHA = 1f
        private const val DANMU_MAX_TEXT_SPEED = 2.5f
        private const val DANMU_MAX_TEXT_STOKE = 20f

        private const val INVALID_VALUE = -1L
    }

    private lateinit var mControlWrapper: ControlWrapper

    private val mDanmakuContext = DanmakuContext.create()
    private val mDanmakuParser = BiliDanmakuParser()
    private val mDanmakuLoader = BiliDanmakuLoader.instance()
    private val mKeywordFilter = KeywordFilter()
    private val mRegexFilter = RegexFilter()

    private var mSeekPosition = INVALID_VALUE

    var mUrl: String? = null
    private var isDanmuLoaded = false

    init {
        showFPS(DanmuConfig.isDanmuDebug())

        initDanmuContext()

        setCallback(object : DrawHandler.Callback {
            override fun drawingFinished() {

            }

            override fun danmakuShown(danmaku: BaseDanmaku?) {

            }

            override fun prepared() {
                post {
                    isDanmuLoaded = true
                    if (mControlWrapper.isPlaying()) {
                        val position =
                            mControlWrapper.getCurrentPosition() + PlayerInitializer.Danmu.offsetPosition
                        start(position)
                    }
                }
            }

            override fun updateTimer(timer: DanmakuTimer?) {

            }
        })
    }

    override fun attach(controlWrapper: ControlWrapper) {
        mControlWrapper = controlWrapper
    }

    override fun onVisibilityChanged(isVisible: Boolean) {

    }

    override fun onPlayStateChanged(playState: PlayState) {
        when (playState) {
            PlayState.STATE_IDLE -> {
                release()
            }
            PlayState.STATE_PLAYING -> {
                if (isPrepared && isPaused) {
                    resume()
                }
            }
            PlayState.STATE_BUFFERING_PAUSED -> {
                if (isPrepared){
                    pause()
                }
            }
            PlayState.STATE_BUFFERING_PLAYING -> {
                if (isPrepared && isPaused) {
                    resume()
                }
            }
            PlayState.STATE_COMPLETED,
            PlayState.STATE_ERROR,
            PlayState.STATE_PAUSED -> {
                if (isPrepared) {
                    pause()
                }
            }
            else -> {
            }
        }
    }

    override fun onProgressChanged(duration: Long, position: Long) {

    }

    override fun onLockStateChanged(isLocked: Boolean) {

    }

    override fun onVideoSizeChanged(videoSize: Point) {

    }

    override fun resume() {
        if (mSeekPosition != INVALID_VALUE) {
            seekTo(mSeekPosition)
            mSeekPosition = INVALID_VALUE
        }
        super.resume()
    }

    fun seekTo(timeMs: Long, skip: Boolean) {
        if (skip) {
            seekTo(timeMs)
        } else {
            mSeekPosition = timeMs
        }
    }

    fun loadDanmu(url: String?) {
        if (url.isNullOrEmpty())
            return
        val danmuFile = File(url)
        if (!danmuFile.exists())
            return

        mUrl = url
        mDanmakuLoader.load(url)
        if (mDanmakuLoader.dataSource != null) {
            mDanmakuParser.load(mDanmakuLoader.dataSource)
            prepare(mDanmakuParser, mDanmakuContext)
            isDanmuLoaded = false
        } else {
            ToastCenter.showOriginalToast("弹幕加载失败")
        }
    }

    fun toggleVis() {
        if (isShown) {
            hide()
        } else {
            show()
        }
    }

    private fun initDanmuContext() {
        //设置禁止重叠
        val overlappingPair: MutableMap<Int, Boolean> = HashMap()
        overlappingPair[BaseDanmaku.TYPE_SCROLL_LR] = true
        overlappingPair[BaseDanmaku.TYPE_SCROLL_RL] = true
        overlappingPair[BaseDanmaku.TYPE_FIX_TOP] = true
        overlappingPair[BaseDanmaku.TYPE_FIX_BOTTOM] = true

        //弹幕更新方式, 0:Choreographer, 1:new Thread, 2:DrawHandler
        val danmuUpdateMethod: Byte =
            if (PlayerInitializer.Danmu.updateInChoreographer) 0 else 2

        mDanmakuContext.apply {
            //合并重复弹幕
            isDuplicateMergingEnabled = true
            //弹幕view开启绘制缓存
            enableDanmakuDrawingCache(true)
            //设置禁止重叠
            mDanmakuContext.preventOverlapping(overlappingPair)
            //使用DrawHandler驱动刷新，避免在高刷新率时时间轴错位
            updateMethod = danmuUpdateMethod
            //添加关键字过滤器
            registerFilter(mKeywordFilter)
            //添加正则过滤器
            registerFilter(mRegexFilter)
        }

        updateDanmuSize()
        updateDanmuSpeed()
        updateDanmuAlpha()
        updateDanmuStoke()
        updateMobileDanmuState()
        updateTopDanmuState()
        updateBottomDanmuState()
        updateMaxLine()
        updateMaxScreenNum()
    }

    fun updateDanmuSize() {
        val progress = PlayerInitializer.Danmu.size / 100f
        val size = progress * DANMU_MAX_TEXT_SIZE
        mDanmakuContext.setScaleTextSize(size)
    }

    fun updateDanmuSpeed() {
        val progress = PlayerInitializer.Danmu.speed / 100f
        var speed = DANMU_MAX_TEXT_SPEED * (1 - progress)
        speed = max(0.1f, speed)
        mDanmakuContext.setScrollSpeedFactor(speed)
    }

    fun updateDanmuAlpha() {
        val progress = PlayerInitializer.Danmu.alpha / 100f
        val alpha = progress * DANMU_MAX_TEXT_ALPHA
        mDanmakuContext.setDanmakuTransparency(alpha)
    }

    fun updateDanmuStoke() {
        val progress = PlayerInitializer.Danmu.stoke / 100f
        val stoke = progress * DANMU_MAX_TEXT_STOKE
        mDanmakuContext.setDanmakuStyle(DANMAKU_STYLE_STROKEN, stoke)
    }

    fun updateMobileDanmuState() {
        mDanmakuContext.r2LDanmakuVisibility = PlayerInitializer.Danmu.mobileDanmu
    }

    fun updateTopDanmuState() {
        mDanmakuContext.ftDanmakuVisibility = PlayerInitializer.Danmu.topDanmu
    }

    fun updateBottomDanmuState() {
        mDanmakuContext.fbDanmakuVisibility = PlayerInitializer.Danmu.bottomDanmu
    }

    fun updateOffsetTime() {
        val newPosition = currentTime + PlayerInitializer.Danmu.offsetPosition
        seekTo(newPosition, mControlWrapper.isPlaying())
    }

    fun updateMaxLine() {
        val maxLine = PlayerInitializer.Danmu.maxLine
        var danmuMaxLineMap: MutableMap<Int, Int>? = null
        if (maxLine > 0) {
            danmuMaxLineMap = mutableMapOf()
            danmuMaxLineMap[BaseDanmaku.TYPE_SCROLL_LR] = maxLine
            danmuMaxLineMap[BaseDanmaku.TYPE_SCROLL_RL] = maxLine
        }
        mDanmakuContext.setMaximumLines(danmuMaxLineMap)
    }

    fun updateMaxScreenNum() {
        mDanmakuContext.setMaximumVisibleSizeInScreen(PlayerInitializer.Danmu.maxNum)
    }

    fun addBlackList(isRegex: Boolean, vararg keyword: String) {
        keyword.forEach {
            if (isRegex) {
                mRegexFilter.addRegex(it)
            } else {
                mKeywordFilter.addKeyword(it)
            }
        }
        notifyFilterChanged()
    }

    fun removeBlackList(isRegex: Boolean, keyword: String) {
        if (isRegex) {
            mRegexFilter.removeRegex(keyword)
        } else {
            mKeywordFilter.removeKeyword(keyword)
        }
        notifyFilterChanged()
    }

    fun setCloudBlockLiveData(cloudBlockLiveData: LiveData<MutableList<DanmuBlockEntity>>?) {
        if (PlayerInitializer.Danmu.cloudBlock) {
            cloudBlockLiveData?.observe(context as LifecycleOwner) {
                it.forEach { entity ->
                    if (entity.isRegex) {
                        mRegexFilter.addRegex(entity.keyword)
                    } else {
                        mKeywordFilter.addKeyword(entity.keyword)
                    }
                }
                notifyFilterChanged()
            }
        }
    }

    fun allowSendDanmu(): Boolean {
        return isDanmuLoaded
    }

    fun addDanmuToView(danmuBean: SendDanmuBean) {
        val type = when {
            danmuBean.isScroll -> BaseDanmaku.TYPE_SCROLL_RL
            danmuBean.isTop -> BaseDanmaku.TYPE_FIX_TOP
            else -> BaseDanmaku.TYPE_FIX_BOTTOM
        }

        val danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(type)
        danmaku.apply {
            text = danmuBean.text
            padding = 5
            isLive = false
            priority = 0
            textColor = danmuBean.color
            underlineColor = Color.GREEN
            time = this@DanmuView.currentTime + 500
        }
        addDanmaku(danmaku)
    }

    fun setSpeed(speed: Float){
        mDanmakuContext.setSpeed(speed)
    }

    private fun notifyFilterChanged() {
        //该方法内部会调用弹幕刷新，能达到相应效果
        mDanmakuContext.addUserHashBlackList()
    }
}