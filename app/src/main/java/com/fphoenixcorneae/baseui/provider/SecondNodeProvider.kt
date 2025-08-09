package com.fphoenixcorneae.baseui.provider

import android.view.View
import androidx.core.view.isVisible
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fphoenixcorneae.baseui.BaseUiNodeAdapter.Companion.EXPAND_COLLAPSE_PAYLOAD
import com.fphoenixcorneae.baseui.R
import com.fphoenixcorneae.baseui.node.SecondNode

class SecondNodeProvider(
    override val itemViewType: Int = 2,
    override val layoutId: Int = R.layout.item_node_second
) : BaseNodeProvider() {
    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        item as SecondNode
        helper.setText(R.id.tvTitle, item.title)
        helper.getView<View>(R.id.ivArrow).isVisible = !item.childNode.isNullOrEmpty()
        if (!item.childNode.isNullOrEmpty()) {
            ProviderHelper.setArrowSpin(helper, item, false)
        }
    }

    override fun convert(helper: BaseViewHolder, item: BaseNode, payloads: List<Any>) {
        if (!item.childNode.isNullOrEmpty()) {
            for (payload in payloads) {
                if (payload is Int && payload == EXPAND_COLLAPSE_PAYLOAD) {
                    // 增量刷新，使用动画变化箭头
                    ProviderHelper.setArrowSpin(helper, item as BaseExpandNode, true)
                }
            }
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        getAdapter()?.expandOrCollapse(
            position = position,
            animate = true,
            notify = true,
            parentPayload = EXPAND_COLLAPSE_PAYLOAD
        )
        data as SecondNode
        data.onClick?.invoke()
    }
}