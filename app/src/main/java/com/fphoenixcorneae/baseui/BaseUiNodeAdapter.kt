package com.fphoenixcorneae.baseui

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.fphoenixcorneae.baseui.node.FirstNode
import com.fphoenixcorneae.baseui.node.SecondNode
import com.fphoenixcorneae.baseui.node.ThirdNode
import com.fphoenixcorneae.baseui.provider.FirstNodeProvider
import com.fphoenixcorneae.baseui.provider.SecondNodeProvider
import com.fphoenixcorneae.baseui.provider.ThirdNodeProvider

class BaseUiNodeAdapter : BaseNodeAdapter() {
    companion object{
        const val EXPAND_COLLAPSE_PAYLOAD = 110
    }
    init {
        addNodeProvider(FirstNodeProvider())
        addNodeProvider(SecondNodeProvider())
        addNodeProvider(ThirdNodeProvider())
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        val node = data[position]
        return when (node) {
            is FirstNode -> 1
            is SecondNode -> 2
            is ThirdNode -> 3
            else -> -1
        }
    }
}