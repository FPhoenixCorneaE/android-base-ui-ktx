package com.fphoenixcorneae.baseui.node

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode

class FirstNode(
    val title: String,
    override val childNode: MutableList<BaseNode>? = null,
    val onClick: (() -> Unit)? = null,
) : BaseExpandNode() {
    init {
        isExpanded = false
    }
}
