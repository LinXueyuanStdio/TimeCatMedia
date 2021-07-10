package com.same.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.timecat.component.router.app.NAV
import com.timecat.page.base.friend.compact.BaseFragmentActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xyoye.common_component.config.RouteTable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/10
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = "main/fragment")
class DownloadActivity : BaseFragmentActivity() {
    @AttrValueAutowiredAnno("url")
    @JvmField
    var url: String = RouteTable.Anime.HomeFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NAV.inject(this)
    }

    override fun createFragment(): Fragment {
        return NAV.fragment(url)
    }
}