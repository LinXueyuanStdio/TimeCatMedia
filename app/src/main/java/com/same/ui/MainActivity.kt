package com.same.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import com.timecat.fake.media.R
import com.timecat.layout.ui.layout.*

class MainActivity : Activity() {

    lateinit var commandLinear: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        val scrollView = HorizontalScrollView(this).apply {
            clipChildren = true
            clipToPadding = false
            layout_width = match_parent
            layout_height = wrap_content
            margin_top = 50
            margin_start = 50
            margin_end = 50
            commandLinear = LinearLayout(context)
            commandLinear.orientation = LinearLayout.HORIZONTAL
            addView(commandLinear)

            for (i in 0..10) {
                commandLinear.addView(Chip(context).apply {
                    margin_end = 10
                    bindAnimation(this)
                    text = "chip $i"
                })
            }
        }
        val frameLayout = FrameLayout(this)
        frameLayout.clipChildren = true
        frameLayout.clipToPadding = true
        frameLayout.addView(scrollView)
        linearLayout.addView(frameLayout)
        linearLayout.addView(createButton("启动动画") {
            resetAnimation()
            commandLinear.removeAllViews()
            for (i in 0..10) {
                commandLinear.addView(Chip(this).apply {
                    margin_end = 10
                    bindAnimation(this)
                    text = "chip $i"
                })
            }
        })
        setContentView(linearLayout)
    }


    //region anim add chip
    protected open val isAnimationEnabled: Boolean
        get() = true
    private var isAnimating = false

    private var animationStartOffset = 0

    private val stopAnimationHandler = Handler(Looper.getMainLooper())
    private val stopAnimationRunnable = Runnable { stopAnimation() }

    fun bindAnimation(holder: Chip) {
        holder.clearAnimation()
        if (isAnimating) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.item_on_load_hori)
                .apply { startOffset = animationStartOffset.toLong() }
            animationStartOffset += ANIMATION_STAGGER_MILLIS
            holder.startAnimation(animation)
            postStopAnimation()
        }
    }

    private fun stopAnimation() {
        stopAnimationHandler.removeCallbacks(stopAnimationRunnable)
        isAnimating = false
        animationStartOffset = 0
    }

    private fun postStopAnimation() {
        stopAnimationHandler.removeCallbacks(stopAnimationRunnable)
        stopAnimationHandler.post(stopAnimationRunnable)
    }

    private fun clearAnimationOfSubView() {
        stopAnimation()
        for (index in 0 until commandLinear.childCount) {
            commandLinear.getChildAt(index).clearAnimation()
        }
    }

    private fun resetAnimation() {
        clearAnimationOfSubView()
        isAnimating = isAnimationEnabled
    }
    //endregion


    companion object {
        private const val ANIMATION_STAGGER_MILLIS = 20
        private const val TRANSLATE_DURATION_MILLIS = 200
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
        return button
    }

    private fun go(path: String) {
    }
}