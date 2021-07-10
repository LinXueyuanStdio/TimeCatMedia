package com.xyoye.user_component.ui.activities.forgot

import androidx.core.widget.addTextChangedListener
import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.common_component.base.BaseActivity
import com.xyoye.common_component.config.RouteTable
import com.xyoye.common_component.utils.showKeyboard
import com.xyoye.user_component.BR
import com.xyoye.user_component.R
import com.xyoye.user_component.databinding.ActivityForgotBinding

@RouterAnno(hostAndPath = RouteTable.User.UserForgot)
class ForgotActivity : BaseActivity<ForgotViewModel, ActivityForgotBinding>() {

    @AttrValueAutowiredAnno("isForgotPassword")
    @JvmField
    var isForgotPassword: Boolean = false

    override fun initViewModel() =
        ViewModelInit(
            BR.viewModel,
            ForgotViewModel::class.java
        )

    override fun getLayoutId() = R.layout.activity_forgot

    override fun initView() {
        NAV.inject(this)

        title = ""

        dataBinding.titleTv.text = if (isForgotPassword) "重置密码" else "找回帐号"
        dataBinding.confirmBt.text = if (isForgotPassword) "重置" else "确定"

        val tips =
            resources.getString(if (isForgotPassword) R.string.tips_reset_password else R.string.tips_retrieve_account)
        dataBinding.tipsTv.text = tips

        viewModel.isForgotPassword.set(isForgotPassword)

        dataBinding.apply {
            if (isForgotPassword) {
                userAccountEt.postDelayed({
                    showKeyboard(userAccountEt)
                }, 200)
            } else {
                userEmailEt.postDelayed({
                    showKeyboard(userEmailEt)
                }, 200)
            }

            userAccountEt.addTextChangedListener {
                userAccountLayout.error = ""
            }
            userEmailEt.addTextChangedListener {
                userAccountLayout.error = ""
            }
        }

        viewModel.accountErrorLiveData.observe(this, {
            dataBinding.userAccountLayout.error = it
        })
        viewModel.emailErrorLiveData.observe(this, {
            dataBinding.userEmailLayout.error = it
        })
        viewModel.requestLiveData.observe(this, {
            finish()
        })
    }
}