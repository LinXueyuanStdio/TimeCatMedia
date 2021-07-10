package com.xyoye.user_component.ui.activities.user_info

import android.view.Menu
import android.view.MenuItem
import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.common_component.base.BaseActivity
import com.xyoye.common_component.config.RouteTable
import com.xyoye.common_component.config.UserConfig
import com.xyoye.common_component.utils.UserInfoHelper
import com.xyoye.common_component.weight.dialog.CommonDialog
import com.xyoye.common_component.weight.dialog.CommonEditDialog
import com.xyoye.data_component.bean.EditBean
import com.xyoye.user_component.BR
import com.xyoye.user_component.R
import com.xyoye.user_component.databinding.ActivityUserInfoBinding
import com.xyoye.user_component.ui.dialog.UpdatePasswordDialog

@RouterAnno(hostAndPath = RouteTable.User.UserInfo)
class UserInfoActivity : BaseActivity<UserInfoViewModel, ActivityUserInfoBinding>() {

    private val updatePasswordDialog = UpdatePasswordDialog { old, new ->
        viewModel.updatePassword(old, new)
        return@UpdatePasswordDialog false
    }

    override fun initViewModel() =
        ViewModelInit(
            BR.viewModel,
            UserInfoViewModel::class.java
        )

    override fun getLayoutId() = R.layout.activity_user_info

    override fun initView() {
        title = "用户信息"

        val loginData = UserInfoHelper.mLoginData ?: return

        val coverIndex = UserConfig.getUserCoverIndex()
        val typedArray = resources.obtainTypedArray(R.array.cover)
        val coverResId = typedArray.getResourceId(coverIndex, 0)
        typedArray.recycle()

        dataBinding.userCoverIv.setImageResource(coverResId)

        dataBinding.screenNameEditLl.setOnClickListener {
            CommonEditDialog(
                EditBean(
                    "修改昵称",
                    "昵称不能为空",
                    "昵称"
                )
            ) {
                viewModel.updateScreenName(it)
            }.show(this)
        }


        dataBinding.passwordEditLl.setOnClickListener {
            updatePasswordDialog.show(this)
        }

        viewModel.updatePasswordLiveData.observe(this) {
            updatePasswordDialog.dismiss()
            NAV.go(RouteTable.User.UserLogin, "userAccount", it)
            finish()
        }

        viewModel.updateScreenNameLiveData.observe(this) {
            dataBinding.userScreenNameTv.text = it
        }

        viewModel.applyLoginData(loginData)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_exit_login -> {
                CommonDialog.Builder().apply {
                    addPositive {
                        it.dismiss()
                        val userAccount: String? = UserInfoHelper.mLoginData?.userName
                        UserInfoHelper.exitLogin()
                        NAV.go(RouteTable.User.UserLogin, "userAccount", userAccount)
                        finish()
                    }
                    addNegative()
                    content = "确定退出登录？"
                }.build().show(this)

            }
        }
        return super.onOptionsItemSelected(item)
    }
}