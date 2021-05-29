package com.xyoye.anime_component.ui.dialog

import com.xyoye.anime_component.R
import com.xyoye.anime_component.databinding.DialogMagnetScreenBinding
import com.xyoye.anime_component.databinding.ItemMagnetScreenBinding
import com.xyoye.common_component.adapter.addItem
import com.xyoye.common_component.adapter.buildAdapter
import com.xyoye.common_component.adapter.initData
import com.xyoye.common_component.extension.grid
import com.xyoye.common_component.weight.dialog.BaseBottomDialog
import com.xyoye.data_component.entity.MagnetScreenEntity
import com.xyoye.data_component.enums.MagnetScreenType

/**
 * Created by xyoye on 2020/10/26.
 */

class MagnetScreenDialog : BaseBottomDialog<DialogMagnetScreenBinding> {
    private lateinit var screenData: MutableList<MagnetScreenEntity>
    private lateinit var screenType: MagnetScreenType
    private lateinit var block: (MagnetScreenEntity) -> Unit

    constructor() : super()

    constructor(
        screenData: MutableList<MagnetScreenEntity>,
        screenType: MagnetScreenType,
        block: (MagnetScreenEntity) -> Unit
    ) : super(true) {
        this.screenData = screenData
        this.screenType = screenType
        this.block = block
    }

    override fun getChildLayoutId() = R.layout.dialog_magnet_screen

    override fun initView(binding: DialogMagnetScreenBinding) {
        setTitle(if (screenType == MagnetScreenType.SUBGROUP) "选择字幕组" else "选择分类")

        setNegativeListener { dismiss() }
        setPositiveVisible(false)

        binding.screenRv.apply {

            layoutManager = grid(2)

            adapter = buildAdapter<MagnetScreenEntity> {
                initData(screenData)

                addItem<MagnetScreenEntity, ItemMagnetScreenBinding>(R.layout.item_magnet_screen) {
                    initView { data, _, _ ->
                        itemBinding.apply {
                            screenTv.text = data.screenName
                            itemLayout.setOnClickListener {
                                dismiss()
                                block.invoke(data)
                            }
                        }
                    }
                }
            }
        }
    }
}