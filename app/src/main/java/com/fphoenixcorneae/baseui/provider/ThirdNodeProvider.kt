package com.fphoenixcorneae.baseui.provider

import android.view.View
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fphoenixcorneae.baseui.R
import com.fphoenixcorneae.baseui.node.FirstNode
import com.fphoenixcorneae.baseui.node.ThirdNode

class ThirdNodeProvider(
    override val itemViewType: Int = 3,
    override val layoutId: Int = R.layout.item_node_third
) : BaseNodeProvider() {
    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        item as ThirdNode
        helper.setText(R.id.tvTitle, item.title)
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        data as ThirdNode
        data.onClick?.invoke()
    }
}