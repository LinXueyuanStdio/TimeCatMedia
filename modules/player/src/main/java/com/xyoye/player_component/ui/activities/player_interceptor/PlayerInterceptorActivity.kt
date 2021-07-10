package com.xyoye.player_component.ui.activities.player_interceptor

import android.content.Intent
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.timecat.component.router.app.ActivityResultCallback
import com.timecat.component.router.app.ForwardCallback
import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.bean.ActivityResult
import com.xiaojinzi.component.impl.RouterErrorResult
import com.xiaojinzi.component.impl.RouterRequest
import com.xiaojinzi.component.impl.RouterResult
import com.xyoye.common_component.base.BaseActivity
import com.xyoye.common_component.config.DanmuConfig
import com.xyoye.common_component.config.RouteTable
import com.xyoye.common_component.weight.ToastCenter
import com.xyoye.common_component.weight.dialog.CommonDialog
import com.xyoye.data_component.bean.PlayParams
import com.xyoye.data_component.enums.MediaType
import com.xyoye.player_component.BR
import com.xyoye.player_component.R
import com.xyoye.player_component.databinding.ActivityPlayerInterceptorBinding

@RouterAnno(hostAndPath = RouteTable.Player.Player)
class PlayerInterceptorActivity : BaseActivity<PlayerInterceptorViewModel, ActivityPlayerInterceptorBinding>() {

    companion object {
        private const val REQUEST_CODE_BIND_DANMU = 1001
    }

    @AttrValueAutowiredAnno("playParams")
    @JvmField
    var playParams: PlayParams? = null

    @AttrValueAutowiredAnno("searchKeyword")
    @JvmField
    var searchKeyword: String? = null

    override fun initViewModel() =
        ViewModelInit(
            BR.viewModel,
            PlayerInterceptorViewModel::class.java
        )

    override fun getLayoutId() = R.layout.activity_player_interceptor

    override fun initStatusBar() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
            .init()
    }

    override fun initView() {
        NAV.inject(this)

        if (playParams == null) {
            ToastCenter.showError("播放参数错误，无法播放视频")
            finish()
            return
        }

        val danmuPath = playParams?.danmuPath

        //本地视频或已有弹幕，不提示选择弹幕
        if (playParams!!.mediaType == MediaType.LOCAL_STORAGE || danmuPath != null) {
            openPlayer(playParams!!)
            return
        }

        //不展示弹窗
        if (!DanmuConfig.isShowDialogBeforePlay()) {
            if (DanmuConfig.isAutoLaunchDanmuBeforePlay()) {
                //自动进入选择弹幕页面
                NAV.raw(RouteTable.Local.BindDanmu)
                    .withString("videoName", playParams!!.videoTitle)
                    .withString("searchKeyword", searchKeyword)
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
            } else {
                //自动进入播放器页面
                openPlayer(playParams!!)
            }
            return
        }

        CommonDialog.Builder().apply {
            cancelable = false
            touchCancelable = false
            content = "检测到视频未绑定弹幕，是否需要绑定弹幕？"
            addPositive("绑定弹幕") {
                it.dismiss()

                //不再展示弹窗，则将当前操作记录为默认行为
                if (!DanmuConfig.isShowDialogBeforePlay()) {
                    DanmuConfig.putAutoLaunchDanmuBeforePlay(true)
                }

                NAV.raw(RouteTable.Local.BindDanmu)
                    .withString("videoName", playParams!!.videoTitle)
                    .withString("searchKeyword", searchKeyword)
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

                //不再展示弹窗，则将当前操作记录为默认行为
                if (!DanmuConfig.isShowDialogBeforePlay()) {
                    DanmuConfig.putAutoLaunchDanmuBeforePlay(false)
                }

                openPlayer(playParams!!)
            }
            addNoShowAgain {
                DanmuConfig.putShowDialogBeforePlay(!it)
            }
        }.build().show(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_BIND_DANMU) {
            if (resultCode == RESULT_OK) {
                val danmuPath = data?.getStringExtra("danmu_path")
                val episodeId = data?.getIntExtra("episode_id", 0) ?: 0
                playParams!!.danmuPath = danmuPath
                playParams!!.episodeId = episodeId
            }
            //绑定弹幕后自动播放
            openPlayer(playParams!!)
        }
        super.onActivityResult(requestCode, resultCode, data)
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