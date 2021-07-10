package com.xyoye.player_component.ui.activities.player_intent

import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.timecat.component.router.app.ActivityResultCallback
import com.timecat.component.router.app.ForwardCallback
import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.bean.ActivityResult
import com.xiaojinzi.component.impl.RouterErrorResult
import com.xiaojinzi.component.impl.RouterRequest
import com.xiaojinzi.component.impl.RouterResult
import com.xyoye.common_component.base.BaseActivity
import com.xyoye.common_component.config.RouteTable
import com.xyoye.common_component.utils.UriUtils
import com.xyoye.common_component.utils.getFileName
import com.xyoye.common_component.weight.dialog.CommonDialog
import com.xyoye.data_component.bean.PlayParams
import com.xyoye.data_component.enums.MediaType
import com.xyoye.player_component.BR
import com.xyoye.player_component.R
import com.xyoye.player_component.databinding.ActivityPlayerIntentBinding

class PlayerIntentActivity : BaseActivity<PlayerIntentViewModel, ActivityPlayerIntentBinding>() {

    companion object {
        private const val REQUEST_CODE_BIND_DANMU = 1002
    }

    private var playParams: PlayParams? = null

    override fun initViewModel() =
        ViewModelInit(
            BR.viewModel,
            PlayerIntentViewModel::class.java
        )

    override fun getLayoutId() = R.layout.activity_player_intent

    override fun initStatusBar() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
            .init()
    }

    override fun initView() {
        dataBinding.exitTv.setOnClickListener {
            finish()
        }

        val videoUri = intent.data
        val videoUriText = videoUri?.toString()
        if (videoUriText.isNullOrEmpty()) {
            viewModel.isParseError.set(true)
            return
        }

        val videoTitle = UriUtils.queryVideoTitle(this, videoUri)
            ?: getFileName(videoUriText)

        playParams = PlayParams(
            videoUriText,
            videoTitle,
            null,
            null,
            0,
            0,
            MediaType.OTHER_STORAGE
        )

        CommonDialog.Builder().apply {
            cancelable = false
            touchCancelable = false
            content = "检测到外部打开视频，是否需要绑定弹幕？"
            addPositive("绑定弹幕") {
                it.dismiss()

                NAV.raw(RouteTable.Local.BindDanmu)
                    .withString("videoName", videoTitle)
                    .requestCode(REQUEST_CODE_BIND_DANMU)
                    .forwardForResult(object : ActivityResultCallback {
                        override fun onCancel(originalRequest: RouterRequest?) {
                        }

                        override fun onError(errorResult: RouterErrorResult) {
                        }

                        override fun onSuccess(result: RouterResult, t: ActivityResult) {
                            if (t.resultCode == RESULT_OK) {
                                val danmuPath = t.data?.getStringExtra("danmu_path")
                                val episodeId = t.data?.getIntExtra("episode_id", 0) ?: 0
                                playParams!!.danmuPath = danmuPath
                                playParams!!.episodeId = episodeId
                            }
                            //绑定弹幕后自动播放
                            openPlayer(playParams!!)
                        }
                    })
            }
            addNegative("直接播放") {
                it.dismiss()

                openPlayer(playParams!!)
            }
        }.build().show(this)
    }

    private fun openPlayer(playParams: PlayParams) {
        NAV.raw(RouteTable.Player.PlayerCenter)
            .withParcelable("playParams", playParams)
            .forward(object : ForwardCallback {
                override fun onError(errorResult: RouterErrorResult) {
                }

                override fun onCancel(originalRequest: RouterRequest?) {
                }

                override fun onSuccess(result: RouterResult) {
                    finish()
                }

                override fun onEvent(successResult: RouterResult?, errorResult: RouterErrorResult?) {
                }
            })
    }
}