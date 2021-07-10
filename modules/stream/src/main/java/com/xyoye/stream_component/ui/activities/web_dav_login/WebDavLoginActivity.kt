package com.xyoye.stream_component.ui.activities.web_dav_login

import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.common_component.base.BaseActivity
import com.xyoye.common_component.config.RouteTable
import com.xyoye.data_component.entity.MediaLibraryEntity
import com.xyoye.stream_component.BR
import com.xyoye.stream_component.R
import com.xyoye.stream_component.databinding.ActivityWebDavLoginBinding
import com.xyoye.stream_component.ui.dialog.WebDavLoginDialog

@RouterAnno(hostAndPath = RouteTable.Stream.WebDavLogin)
class WebDavLoginActivity : BaseActivity<WebDavLoginViewModel, ActivityWebDavLoginBinding>() {

    @AttrValueAutowiredAnno("editData")
    @JvmField
    var editData: MediaLibraryEntity? = null

    override fun initViewModel() =
        ViewModelInit(
            BR.viewModel,
            WebDavLoginViewModel::class.java
        )

    override fun getLayoutId() = R.layout.activity_web_dav_login

    override fun initView() {
        NAV.inject(this)

        WebDavLoginDialog(
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