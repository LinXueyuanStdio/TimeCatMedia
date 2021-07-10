package com.xyoye.local_component.ui.activities.play_history

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.common_component.adapter.addEmptyView
import com.xyoye.common_component.adapter.addItem
import com.xyoye.common_component.adapter.buildAdapter
import com.xyoye.common_component.base.BaseActivity
import com.xyoye.common_component.config.RouteTable
import com.xyoye.common_component.extension.*
import com.xyoye.common_component.utils.*
import com.xyoye.common_component.weight.ToastCenter
import com.xyoye.common_component.weight.dialog.FileManagerDialog
import com.xyoye.data_component.bean.PlayParams
import com.xyoye.data_component.entity.PlayHistoryEntity
import com.xyoye.data_component.enums.FileManagerAction
import com.xyoye.data_component.enums.MediaType
import com.xyoye.local_component.BR
import com.xyoye.local_component.R
import com.xyoye.local_component.databinding.ActivityPlayHistoryBinding
import com.xyoye.local_component.databinding.ItemPlayHistoryBinding
import com.xyoye.local_component.ui.dialog.MagnetPlayDialog
import com.xyoye.local_component.ui.dialog.StreamLinkDialog
import com.xyoye.local_component.utils.MediaTypeUtil
import java.io.File

@RouterAnno(hostAndPath = RouteTable.Local.PlayHistory)
class PlayHistoryActivity : BaseActivity<PlayHistoryViewModel, ActivityPlayHistoryBinding>() {

    @AttrValueAutowiredAnno("typeValue")
    @JvmField
    var typeValue: String = MediaType.LOCAL_STORAGE.value

    private var removeItem: MenuItem? = null
    private var selectAllItem: MenuItem? = null
    private lateinit var mediaType: MediaType
    private lateinit var mTitleText: String

    override fun initViewModel() =
        ViewModelInit(
            BR.viewModel,
            PlayHistoryViewModel::class.java
        )

    override fun getLayoutId() = R.layout.activity_play_history

    override fun initView() {
        NAV.inject(this)

        mediaType = MediaType.fromValue(typeValue)

        mTitleText = when (mediaType) {
            MediaType.MAGNET_LINK -> "磁链播放"
            MediaType.STREAM_LINK -> "串流播放"
            else -> "播放历史"
        }
        title = mTitleText

        initRv()

        //标题返回事件
        mToolbar?.setNavigationOnClickListener {
            if (viewModel.isEditMode.get()) {
                viewModel.isEditMode.set(false)
                updateEditModeView()
                return@setNavigationOnClickListener
            }
            finish()
        }

        //添加播放事件
        dataBinding.addLinkBt.setOnClickListener {
            if (mediaType == MediaType.STREAM_LINK) {
                showStreamDialog()
            } else if (mediaType == MediaType.MAGNET_LINK) {
                showMagnetDialog()
            }
        }

        //初始化数据源
        viewModel.initHistoryType(mediaType)
        viewModel.showAddButton.set(mediaType == MediaType.MAGNET_LINK || mediaType == MediaType.STREAM_LINK)
        viewModel.playHistoryLiveData.observe(this) {
            dataBinding.playHistoryRv.setData(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.menu_history, it)
            removeItem = it.findItem(R.id.remove_history_item).apply { isVisible = false }
            selectAllItem = it.findItem(R.id.select_all_history_item).apply { isVisible = false }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.remove_history_item) {
            //清空选中记录
            val historyList = mutableListOf<PlayHistoryEntity>()
            viewModel.playHistoryLiveData.value?.forEach {
                if (it.checked) {
                    historyList.add(it)
                }
            }
            viewModel.removeHistory(historyList)
        } else if (item.itemId == R.id.select_all_history_item) {
            //全选记录
            viewModel.playHistoryLiveData.value?.forEach {
                it.checked = true
            }
            dataBinding.playHistoryRv.adapter?.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewModel.isEditMode.get()) {
                viewModel.isEditMode.set(false)
                updateEditModeView()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initRv() {
        dataBinding.playHistoryRv.apply {
            layoutManager = vertical()

            adapter = buildAdapter<PlayHistoryEntity> {
                addEmptyView(R.layout.layout_empty) {
                    initEmptyView {
                        itemBinding.emptyTv.text = "暂无播放记录"
                    }
                }

                addItem<PlayHistoryEntity, ItemPlayHistoryBinding>(R.layout.item_play_history) {
                    initView { data, _, _ ->
                        itemBinding.apply {
                            //磁链是否失效
                            val isInvalid = isHistoryInvalid(data)
                            if (isInvalid) {
                                videoTypeTv.text = "失效"
                                videoTypeTv.setTextColorRes(R.color.text_red)
                            } else {
                                videoTypeTv.text = MediaTypeUtil.getText(data.mediaType)
                                videoTypeTv.setTextColorRes(R.color.text_theme)
                            }

                            videoDescribeTv.isVisible =
                                data.mediaType == MediaType.MAGNET_LINK && !data.torrentTitle.isNullOrEmpty()
                            videoDescribeTv.text = data.torrentTitle

                            videoTypeIv.setImageDrawable(getMediaCover(data.mediaType, isInvalid))
                            videoNameTv.text = data.videoName

                            videoUrlTv.text = if (data.mediaType == MediaType.MAGNET_LINK)
                                getFileName(data.torrentPath)
                            else
                                data.url

                            playProgressTv.text =
                                getProgress(data.videoPosition, data.videoDuration)
                            val timeText = "时间：${date2Str(data.playTime, "yy-MM-dd HH:mm")}"
                            playTimeTv.text = timeText

                            //编辑模式
                            editCb.isVisible = viewModel.isEditMode.get()
                            editCb.isChecked = data.checked

                            itemLayout.setOnClickListener {
                                //防止快速点击
                                if (FastClickFilter.isNeedFilter())
                                    return@setOnClickListener

                                //编辑模式
                                if (viewModel.isEditMode.get()) {
                                    data.checked = !data.checked
                                    editCb.isChecked = data.checked
                                    return@setOnClickListener
                                }
                                //普通模式
                                if (isInvalid) {
                                    ToastCenter.showError("记录已失效，无法播放")
                                    return@setOnClickListener
                                }
                                play(data)
                            }

                            itemLayout.setOnLongClickListener {
                                if (!viewModel.isEditMode.get()) {
                                    viewModel.isEditMode.set(true)
                                    updateEditModeView()
                                }
                                return@setOnLongClickListener true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showStreamDialog() {
        val playParams = PlayParams(
            "",
            "",
            null,
            null,
            0,
            0,
            mediaType
        )

        StreamLinkDialog { link, header ->
            playParams.videoPath = link
            playParams.videoTitle = getFileName(link)
            playParams.header = header

            //串流播放
            NAV.go(RouteTable.Player.Player, "playParams", playParams as Parcelable)
        }.show(this)
    }

    private fun showMagnetDialog() {
        MagnetPlayDialog(magnetCallback = {
            //磁链选择播放
            NAV.go(RouteTable.Download.PlaySelection, "magnetLink", it)
        }, torrentCallback = {
            //选择本地种子文件
            FileManagerDialog(FileManagerAction.ACTION_SELECT_TORRENT) {
                //磁链选择播放
                NAV.go(RouteTable.Download.PlaySelection, "torrentPath", it)
            }.show(this)
        }).show(this)
    }

    private fun play(entity: PlayHistoryEntity) {
        //磁链直接播放
        if (entity.mediaType == MediaType.MAGNET_LINK) {
            NAV.raw(RouteTable.Download.PlaySelection)
                .withString("torrentPath", entity.torrentPath)
                .withString("torrentTitle", entity.torrentTitle)
                .withInt("torrentFileIndex", entity.torrentFileIndex)
                .navigation()
            return
        }

        //普通记录直接播放
        val playParams = PlayParams(
            entity.url,
            entity.videoName,
            entity.danmuPath,
            entity.subtitlePath,
            entity.videoPosition,
            entity.episodeId,
            entity.mediaType,
            StreamHeaderUtil.string2Header(entity.header)
        )
        NAV.go(RouteTable.Player.Player, "playParams", playParams as Parcelable)
    }

    private fun isHistoryInvalid(entity: PlayHistoryEntity): Boolean {
        return when (entity.mediaType) {
            MediaType.MAGNET_LINK -> {
                //磁链种子文件丢失
                if (entity.torrentPath.isNullOrEmpty() || entity.torrentFileIndex == -1) {
                    return true
                }
                val torrentFile = File(entity.torrentPath!!)
                return !torrentFile.exists()
            }
            MediaType.SMB_SERVER,
            MediaType.FTP_SERVER -> true
            else -> false
        }
    }

    private fun getProgress(position: Long, duration: Long): String {
        val progress = "${(position.toFloat() / duration.toFloat() * 100f).toInt()}%"
        return "进度：${formatDuration(position)}/${formatDuration(duration)} （$progress）"
    }

    private fun getMediaCover(itemMediaType: MediaType, isInvalid: Boolean): Drawable? {
        val coverId = if (mediaType == MediaType.OTHER_STORAGE) {
            MediaTypeUtil.getCover(itemMediaType)
        } else {
            R.drawable.ic_play_history
        }
        val coverDrawable = coverId.toResDrawable()
        if (isInvalid) {
            coverDrawable?.colorFilter = PorterDuffColorFilter(
                R.color.red.toResColor(),
                PorterDuff.Mode.SRC_ATOP
            )
        }
        return coverDrawable
    }

    private fun updateEditModeView() {
        val isEditMode = viewModel.isEditMode.get()

        viewModel.showAddButton.set(!isEditMode)

        removeItem?.isVisible = isEditMode
        selectAllItem?.isVisible = isEditMode
        dataBinding.playHistoryRv.adapter?.notifyDataSetChanged()

        title = if (isEditMode) {
            "删除播放记录"
        } else {
            mTitleText
        }

        if (!isEditMode) {
            viewModel.playHistoryLiveData.value?.forEach {
                it.checked = false
            }
        }
    }
}