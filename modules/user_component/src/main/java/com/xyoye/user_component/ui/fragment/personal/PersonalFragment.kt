package com.xyoye.user_component.ui.fragment.personal

import android.os.Parcelable
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.common_component.base.BaseFragment
import com.xyoye.common_component.bridge.LoginObserver
import com.xyoye.common_component.config.RouteTable
import com.xyoye.common_component.config.UserConfig
import com.xyoye.common_component.extension.setTextColorRes
import com.xyoye.common_component.utils.UserInfoHelper
import com.xyoye.data_component.data.LoginData
import com.xyoye.user_component.BR
import com.xyoye.user_component.R
import com.xyoye.user_component.databinding.FragmentPersonalBinding
import com.xyoye.user_component.ui.dialog.UserCoverDialog

/**
 * Created by xyoye on 2020/7/28.
 */

@RouterAnno(hostAndPath = RouteTable.User.PersonalFragment)
class PersonalFragment : BaseFragment<PersonalFragmentViewModel, FragmentPersonalBinding>() {

    override fun initViewModel() = ViewModelInit(
        BR.viewModel,
        PersonalFragmentViewModel::class.java
    )

    override fun getLayoutId() = R.layout.fragment_personal

    override fun initView() {
        dataBinding.userCoverIv.setImageResource(getDefaultCoverResId())

        initClick()

        applyLoginData(null)

        viewModel.relationLiveData.observe(this) {
            dataBinding.followAnimeTv.text = it.first.toString()
            dataBinding.followAnimeTv.setTextColorRes(R.color.text_pink)
            dataBinding.cloudHistoryTv.text = it.second.toString()
        }

        UserInfoHelper.loginLiveData.observe(this) {
            applyLoginData(it)
        }

        if (mAttachActivity is LoginObserver) {
            (mAttachActivity as LoginObserver).getLoginLiveData().observe(this) {
                applyLoginData(it)
            }
        }
    }

    private fun applyLoginData(loginData: LoginData?) {
        if (loginData != null) {
            dataBinding.userNameTv.text = loginData.screenName
            dataBinding.tipsLoginBt.isVisible = false
            viewModel.getUserRelationInfo()
        } else {
            dataBinding.userNameTv.text = "登录账号"
            dataBinding.tipsLoginBt.isVisible = true
            dataBinding.followAnimeTv.text = resources.getText(R.string.text_default_count)
            dataBinding.followAnimeTv.setTextColorRes(R.color.text_black)
            dataBinding.cloudHistoryTv.text = resources.getText(R.string.text_default_count)
        }
    }

    private fun getDefaultCoverResId(): Int {
        val coverArray = resources.getIntArray(R.array.cover)
        var coverIndex = UserConfig.getUserCoverIndex()
        if (coverIndex == -1) {
            coverIndex = coverArray.indices.random()
            UserConfig.putUserCoverIndex(coverIndex)
        }
        val typedArray = resources.obtainTypedArray(R.array.cover)
        val coverResId = typedArray.getResourceId(coverIndex, 0)
        typedArray.recycle()
        return coverResId
    }

    private fun initClick() {

        dataBinding.userCoverIv.setOnClickListener {
            UserCoverDialog {
                val typedArray = resources.obtainTypedArray(R.array.cover)
                val coverResId = typedArray.getResourceId(it, 0)
                typedArray.recycle()
                UserConfig.putUserCoverIndex(it)
                dataBinding.userCoverIv.setImageResource(coverResId)
            }.show(this)
        }

        dataBinding.userAccountCl.setOnClickListener {
            if (!checkLoggedIn())
                return@setOnClickListener

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                mAttachActivity, dataBinding.userCoverIv, dataBinding.userCoverIv.transitionName
            )

            NAV.raw(mAttachActivity, RouteTable.User.UserInfo)
                .options(options.toBundle())
                .forward()
        }

        dataBinding.followAnimeLl.setOnClickListener {
            if (!checkLoggedIn())
                return@setOnClickListener

            NAV.go(RouteTable.Anime.AnimeFollow, "followData", viewModel.followData as Parcelable)
        }

        dataBinding.cloudHistoryLl.setOnClickListener {
            if (!checkLoggedIn())
                return@setOnClickListener

            NAV.go(RouteTable.Anime.AnimeHistory, "historyData", viewModel.historyData as Parcelable)
        }

        dataBinding.playerSettingLl.setOnClickListener {
            NAV.go(RouteTable.User.SettingPlayer)
        }

        dataBinding.scanManagerLl.setOnClickListener {
            NAV.go(RouteTable.User.ScanManager)
        }

        dataBinding.cacheManagerLl.setOnClickListener {
            NAV.go(RouteTable.User.CacheManager)
        }

        dataBinding.commonlyManagerLl.setOnClickListener {
            NAV.go(RouteTable.User.CommonManager)
        }

        dataBinding.bilibiliDanmuLl.setOnClickListener {
            NAV.go(RouteTable.Local.BiliBiliDanmu)
        }

        dataBinding.shooterSubtitleLl.setOnClickListener {
            NAV.go(RouteTable.Local.ShooterSubtitle)
        }

        dataBinding.appSettingLl.setOnClickListener {
            NAV.go(RouteTable.User.SettingApp)
        }
    }

    /**
     * 检查是否已登录
     */
    private fun checkLoggedIn(): Boolean {
        if (!UserConfig.isUserLoggedIn()) {
            NAV.go(RouteTable.User.UserLogin)
            return false
        }
        return true
    }
}