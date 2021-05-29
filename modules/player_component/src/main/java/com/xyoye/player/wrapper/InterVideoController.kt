package com.xyoye.player.wrapper

/**
 * Created by xyoye on 2020/11/1.
 *
 * 控制器视图包括：上、下控制器，锁定按钮，截图按钮
 */

interface InterVideoController {

    /**
     * 开启控制器视图隐藏倒计时
     */
    fun startFadeOut()

    /**
     * 停止控制器视图隐藏倒计时
     */
    fun stopFadeOut()

    /**
     * 控制器视图是否正在显示
     */
    fun isControllerShowing(): Boolean

    /**
     * 设置播放器是否锁定
     */
    fun setLocked(locked: Boolean)

    /**
     * 播放器是否已被锁定
     */
    fun isLocked(): Boolean

    /**
     * 开启进度更新线程
     */
    fun startProgress()

    /**
     * 停止进度更新线程
     */
    fun stopProgress()

    /**
     * 隐藏控制器视图
     */
    fun hideController()

    /**
     * 显示控制器视图
     */
    fun showController()

    /**
     * 播放器销毁，预留方法
     */
    fun destroy()
}