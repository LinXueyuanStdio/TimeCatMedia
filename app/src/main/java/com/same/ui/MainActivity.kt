package com.same.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.timecat.component.router.app.NAV
import com.xyoye.anime_component.ui.activities.main.MainActivity
import com.xyoye.common_component.config.RouteTable

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        linearLayout.addView(createButton("anim") {
            NAV.go("main/fragment", "url", RouteTable.Anime.HomeFragment)
        })
//        linearLayout.addView(createButton("anim_download") {
//            startActivity(Intent(this, com.xyoye.download_component.MainActivity::class.java))
//        })
        linearLayout.addView(createButton("anim_local") {
            NAV.go("main/fragment", "url", RouteTable.Local.MediaFragment)
        })
        linearLayout.addView(createButton("anim_player") {
            NAV.go(RouteTable.Player.PlayerCenter)
        })
        linearLayout.addView(createButton("anim_stream") {
            NAV.go(RouteTable.Stream.RemoteControl)
        })
        setContentView(linearLayout)
    }

    private fun createButton(name: String, path: String): Button {
        val button = createButton(name)
        button.setOnClickListener { go(path) }
        return button
    }

    private fun createButton(name: String, onClickListener: View.OnClickListener): Button {
        val button = createButton(name)
        button.setOnClickListener(onClickListener)
        return button
    }

    private fun createButton(name: String): Button {
        val button = Button(this)
        button.text = name
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        button.layoutParams = layoutParams
        button.gravity = Gravity.CENTER
        button.isAllCaps = false
        return button
    }

    private fun go(path: String) {
    }
}