package com.xyoye.local_component.ui.fragment.media

import android.Manifest
import android.os.Parcelable
import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.common_component.adapter.addItem
import com.xyoye.common_component.adapter.buildAdapter
import com.xyoye.common_component.base.BaseFragment
import com.xyoye.common_component.config.RouteTable
import com.xyoye.common_component.extension.setAutoSizeText
import com.xyoye.common_component.extension.setData
import com.xyoye.common_component.extension.vertical
import com.xyoye.common_component.permission.requestPermissions
import com.xyoye.common_component.weight.BottomActionDialog
import com.xyoye.common_component.weight.ToastCenter
import com.xyoye.common_component.weight.dialog.CommonDialog
import com.xyoye.data_component.bean.SheetActionBean
import com.xyoye.data_component.entity.MediaLibraryEntity
import com.xyoye.data_component.enums.MediaType
import com.xyoye.data_component.enums.SheetActionType
import com.xyoye.local_component.BR
import com.xyoye.local_component.R
import com.xyoye.local_component.databinding.FragmentMediaBinding
import com.xyoye.local_component.databinding.ItemMediaLibraryBinding
import com.xyoye.local_component.utils.MediaTypeUtil
import java.io.Serializable

/**
 * Created by xyoye on 2020/7/27.
 */

@RouterAnno(hostAndPath = RouteTable.Local.MediaFragment)
class MediaFragment : BaseFragment<MediaViewModel, FragmentMediaBinding>() {
    companion object {
        private const val ACTION_ADD_FTP_LIBRARY = 1
        private const val ACTION_ADD_SMB_LIBRARY = 2
        private const val ACTION_ADD_WEBDAV_LIBRARY = 3
        private const val ACTION_ADD_REMOTE_LIBRARY = 4


        private const val ACTION_EDIT_STORAGE = 11
        private const val ACTION_DELETE_STORAGE = 12
    }

    override fun initViewModel() = ViewModelInit(
        BR.viewModel,
        MediaViewModel::class.java
    )

    override fun getLayoutId() = R.layout.fragment_media

    override fun initView() {
        viewModel.initLocalStorage()

        initRv()

        dataBinding.addMediaStorageBt.setOnClickListener {
            addMediaStorage()
        }

        viewModel.mediaLibLiveData.observe(this, {
            dataBinding.mediaLibRv.setData(it)
        })
    }

    private fun initRv() {
        dataBinding.mediaLibRv.apply {
            layoutManager = vertical()

            adapter = buildAdapter<MediaLibraryEntity> {
                addItem<MediaLibraryEntity, ItemMediaLibraryBinding>(R.layout.item_media_library) {
                    initView { data, _, _ ->
                        itemBinding.apply {
                            libraryNameTv.setAutoSizeText(data.displayName, 12, 18)
                            libraryUrlTv.text = when (data.mediaType) {
                                MediaType.STREAM_LINK,
                                MediaType.MAGNET_LINK,
                                MediaType.REMOTE_STORAGE,
                                MediaType.SMB_SERVER -> data.describe
                                else -> data.url
                            }
                            libraryCoverIv.setImageResource(
                                MediaTypeUtil.getCover(data.mediaType)
                            )
                            itemLayout.setOnClickListener {
                                requestPermissions(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) {
                                    onGranted {
                                        launchMediaStorage(data)
                                    }
                                    onDenied { _, _ ->
                                        ToastCenter.showError("获取文件读取权限失败，无法打开媒体库")
                                    }
                                }

                            }
                            itemLayout.setOnLongClickListener {
                                if (data.mediaType == MediaType.WEBDAV_SERVER
                                    || data.mediaType == MediaType.FTP_SERVER
                                    || data.mediaType == MediaType.SMB_SERVER
                                    || data.mediaType == MediaType.REMOTE_STORAGE
                                ) {
                                    showEditStorageDialog(data)
                                }
                                true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addMediaStorage() {
        BottomActionDialog(
            mutableListOf(
                SheetActionBean(
                    ACTION_ADD_FTP_LIBRARY,
                    "FTP媒体库",
                    R.drawable.ic_ftp_storage
                ),
                SheetActionBean(
                    ACTION_ADD_SMB_LIBRARY,
                    "SMB媒体库",
                    R.drawable.ic_smb_storage
                ),
                SheetActionBean(
                    ACTION_ADD_WEBDAV_LIBRARY,
                    "WebDav媒体库",
                    R.drawable.ic_webdav_storage
                ),
                SheetActionBean(
                    ACTION_ADD_REMOTE_LIBRARY,
                    "远程媒体库",
                    R.drawable.ic_remote_storage
                )
            ),
            SheetActionType.VERTICAL,
            "新增网络媒体库"
        ) {
            val routePath = when (it) {
                ACTION_ADD_WEBDAV_LIBRARY -> RouteTable.Stream.WebDavLogin
                ACTION_ADD_FTP_LIBRARY -> RouteTable.Stream.FTPLogin
                ACTION_ADD_SMB_LIBRARY -> RouteTable.Stream.SmbLogin
                ACTION_ADD_REMOTE_LIBRARY -> RouteTable.Stream.RemoteLogin
                else -> throw IllegalArgumentException()
            }

            NAV.go(routePath)

            return@BottomActionDialog true
        }.show(this)
    }

    private fun launchMediaStorage(data: MediaLibraryEntity) {
        when (data.mediaType) {
            MediaType.LOCAL_STORAGE -> {
                NAV.go(RouteTable.Local.LocalMediaStorage)
            }
            MediaType.STREAM_LINK, MediaType.MAGNET_LINK, MediaType.OTHER_STORAGE -> {
                NAV.go(RouteTable.Local.PlayHistory, "typeValue", data.mediaType.value as Serializable)
            }
            MediaType.WEBDAV_SERVER -> {
                NAV.go(RouteTable.Stream.WebDavFile, "webDavData", data as Parcelable)
            }
            MediaType.FTP_SERVER -> {
                NAV.go(RouteTable.Stream.FTPFile, "ftpData", data as Parcelable)
            }
            MediaType.SMB_SERVER -> {
                NAV.go(RouteTable.Stream.SmbFile, "smbData", data as Parcelable)
            }
            MediaType.REMOTE_STORAGE -> {
                NAV.go(RouteTable.Stream.RemoteFile, "remoteData", data as Parcelable)
            }
        }
    }

    private fun showEditStorageDialog(data: MediaLibraryEntity) {
        BottomActionDialog(
            mutableListOf(
                SheetActionBean(
                    ACTION_EDIT_STORAGE,
                    "编辑媒体库",
                    R.drawable.ic_edit_storage
                ),
                SheetActionBean(
                    ACTION_DELETE_STORAGE,
                    "删除媒体库",
                    R.drawable.ic_delete_storage
                )
            ),
            SheetActionType.VERTICAL
        ) {
            if (it == ACTION_EDIT_STORAGE) {
                val routePath = when (data.mediaType) {
                    MediaType.WEBDAV_SERVER -> RouteTable.Stream.WebDavLogin
                    MediaType.FTP_SERVER -> RouteTable.Stream.FTPLogin
                    MediaType.SMB_SERVER -> RouteTable.Stream.SmbLogin
                    MediaType.REMOTE_STORAGE -> RouteTable.Stream.RemoteLogin
                    else -> throw IllegalArgumentException()
                }
                NAV.go(routePath, "editData", data as Parcelable)
            } else if (it == ACTION_DELETE_STORAGE) {
                showDeleteStorageDialog(data)
            }
            return@BottomActionDialog true
        }.show(this)
    }

    private fun showDeleteStorageDialog(data: MediaLibraryEntity) {
        CommonDialog.Builder()
            .apply {
                content = "确认删除以下媒体库?\n\n${data.displayName}"
                positiveText = "确认"
                addPositive { dialog ->
                    dialog.dismiss()
                    viewModel.deleteStorage(data)
                }
                addNegative()
            }.build().show(this)
    }
}