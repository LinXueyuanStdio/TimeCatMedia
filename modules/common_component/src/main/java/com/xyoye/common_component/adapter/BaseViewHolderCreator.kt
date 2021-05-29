package com.xyoye.common_component.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created by xyoye on 2020/7/7.
 */

abstract class BaseViewHolderCreator<T : Any, V : ViewDataBinding> {
    abstract fun isForViewType(data: T?, position: Int): Boolean

    abstract fun getResourceId(): Int

    abstract fun onBindViewHolder(
        data: T?,
        position: Int,
        creator: BaseViewHolderCreator<T, out ViewDataBinding>
    )

    lateinit var itemView: View
    lateinit var itemBinding: V

    fun registerItemView(itemView: View) {
        this.itemView = itemView
        this.itemBinding = DataBindingUtil.getBinding(itemView)!!
    }
}