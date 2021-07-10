package com.xyoye.download_component.ui.activities.download_selection

import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.common_component.base.BaseActivity
import com.xyoye.common_component.config.RouteTable
import com.xyoye.download_component.BR
import com.xyoye.download_component.R
import com.xyoye.download_component.databinding.ActivityDownloadSelectionBinding
import com.xyoye.download_component.ui.dialog.DownloadSelectionDialog

@RouterAnno(hostAndPath = RouteTable.Download.DownloadSelection)
class DownloadSelectionActivity :
    BaseActivity<DownloadSelectionViewModel, ActivityDownloadSelectionBinding>() {

    @AttrValueAutowiredAnno("torrentPath")
    @JvmField
    var torrentPath: String? = null

    override fun initViewModel() =
        ViewModelInit(
            BR.viewModel,
            DownloadSelectionViewModel::class.java
        )

    override fun initStatusBar() {

    }

    override fun getLayoutId() = R.layout.activity_download_selection

    override fun initView() {
        NAV.inject(this)
        title = ""

        DownloadSelectionDialog(torrentPath) {
            if (it != null) {
                NAV.raw(RouteTable.Download.DownloadList)
                    .withString("torrentPath", torrentPath)
                    .withByteArray("selection", boolean2byte(it))
                    .navigation()
            }
        }.show(this)
    }

    private fun boolean2byte(booleanList: MutableList<Boolean>): ByteArray {
        val byteArray = ByteArray(booleanList.size)
        for ((index, value) in booleanList.withIndex()) {
            byteArray[index] = if (value) 1 else 0
        }
        return byteArray
    }
}