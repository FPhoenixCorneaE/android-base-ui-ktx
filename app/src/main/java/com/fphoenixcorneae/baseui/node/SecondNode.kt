package com.fphoenixcorneae.baseui.node

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode

class SecondNode(
    override val childNode: MutableList<BaseNode>?,
    val title: String,
    val onClick: (() -> Unit)? = null,
) : BaseExpandNode() {
    init {
        isExpanded = false
    }
}
