package com.xyoye.anime_component.ui.activities.anime_tag

import com.timecat.component.router.app.NAV
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.anime_component.BR
import com.xyoye.anime_component.R
import com.xyoye.anime_component.databinding.ActivityAnimeTagBinding
import com.xyoye.anime_component.ui.adapter.AnimeAdapter
import com.xyoye.common_component.base.BaseActivity
import com.xyoye.common_component.config.RouteTable
import com.xyoye.common_component.extension.gridEmpty
import com.xyoye.common_component.extension.setData
import com.xyoye.common_component.extension.toResColor
import com.xyoye.common_component.utils.dp2px
import com.xyoye.common_component.utils.view.ItemDecorationDrawable
import com.xyoye.common_component.weight.ToastCenter

@RouterAnno(hostAndPath = RouteTable.Anime.AnimeTag)
class AnimeTagActivity : BaseActivity<AnimeTagViewModel, ActivityAnimeTagBinding>() {
    private val animeAdapter = AnimeAdapter.getAdapter(this)

    @AttrValueAutowiredAnno("tagId")
    @JvmField
    var tagId: Int = -1

    @AttrValueAutowiredAnno("tagName")
    @JvmField
    var tagName: String? = null

    override fun initViewModel() =
        ViewModelInit(
            BR.viewModel,
            AnimeTagViewModel::class.java
        )

    override fun getLayoutId() = R.layout.activity_anime_tag

    override fun initView() {
        NAV.inject(this)

        title = "番剧标签"

        if (tagId == -1 || tagName == null) {
            ToastCenter.showError("标签数据异常")
            return
        }
        title = tagName

        dataBinding.tagAnimeRv.apply {

            layoutManager = gridEmpty(3)

            adapter = animeAdapter

            val pxValue = dp2px(10)
            addItemDecoration(
                ItemDecorationDrawable(
                    pxValue,
                    pxValue,
                    R.color.item_bg_color.toResColor()
                )
            )
        }


        viewModel.getAnimeByTag(tagId)

        viewModel.tagAnimeLiveData.observe(this) {
            dataBinding.tagAnimeRv.setData(it.animes)
        }
    }
}