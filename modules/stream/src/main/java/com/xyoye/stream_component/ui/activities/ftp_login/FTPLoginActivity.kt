package com.xyoye.stream_component.ui.activities.ftp_login

import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.common_component.base.BaseActivity
import com.xyoye.common_component.config.RouteTable
import com.xyoye.data_component.entity.MediaLibraryEntity
import com.xyoye.stream_component.BR
import com.xyoye.stream_component.R
import com.xyoye.stream_component.databinding.ActivityFtpLoginBinding
import com.xyoye.stream_component.ui.dialog.FTPLoginDialog

@RouterAnno(hostAndPath = RouteTable.Stream.FTPLogin)
class FTPLoginActivity : BaseActivity<FTPLoginViewModel, ActivityFtpLoginBinding>() {

    @AttrValueAutowiredAnno("editData")
    @JvmField
    var editData: MediaLibraryEntity? = null

    override fun initViewModel() =
        ViewModelInit(
            BR.viewModel,
            FTPLoginViewModel::class.java
        )

    override fun getLayoutId() = R.layout.activity_ftp_login

    override fun initView() {
        NAV.inject(this)

        FTPLoginDialog(
            editData,
            addMediaStorage = {
                viewModel.addFTPStorage(editData, it)
            },
            testConnect = {
                viewModel.testConnect(it)
            },
            viewModel.testConnectLiveData
        ).show(this)
    }
}