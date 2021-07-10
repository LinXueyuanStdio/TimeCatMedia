package com.xyoye.stream_component.ui.activities.smb_login

import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.common_component.base.BaseActivity
import com.xyoye.common_component.config.RouteTable
import com.xyoye.data_component.entity.MediaLibraryEntity
import com.xyoye.stream_component.BR
import com.xyoye.stream_component.R
import com.xyoye.stream_component.databinding.ActivitySmbLoginBinding
import com.xyoye.stream_component.ui.dialog.SmbLoginDialog

@RouterAnno(hostAndPath = RouteTable.Stream.SmbLogin)
class SmbLoginActivity : BaseActivity<SmbLoginViewModel, ActivitySmbLoginBinding>() {

    @AttrValueAutowiredAnno("editData")
    @JvmField
    var editData: MediaLibraryEntity? = null

    override fun initViewModel() =
        ViewModelInit(
            BR.viewModel,
            SmbLoginViewModel::class.java
        )

    override fun getLayoutId() = R.layout.activity_smb_login

    override fun initView() {
        NAV.inject(this)

        SmbLoginDialog(
            editData,
            addMediaStorage = {
                viewModel.addWebDavStorage(editData, it)
            },
            testConnect = {
                viewModel.testConnect(it)
            },
            viewModel.testConnectLiveData
        ).show(this)
    }
}