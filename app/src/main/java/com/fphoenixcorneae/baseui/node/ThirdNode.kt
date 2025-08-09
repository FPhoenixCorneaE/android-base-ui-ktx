package com.fphoenixcorneae.baseui.node

import com.chad.library.adapter.base.entity.node.BaseNode

class ThirdNode(val title: String, val onClick: (() -> Unit)? = null) : BaseNode() {
    override val childNode: MutableList<BaseNode>?
        get() = null
}
