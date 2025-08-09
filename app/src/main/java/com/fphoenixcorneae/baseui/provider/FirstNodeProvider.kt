package com.fphoenixcorneae.baseui.provider

import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fphoenixcorneae.baseui.BaseUiNodeAdapter.Companion.EXPAND_COLLAPSE_PAYLOAD
import com.fphoenixcorneae.baseui.R
import com.fphoenixcorneae.baseui.node.FirstNode

class FirstNodeProvider(
    override val itemViewType: Int = 1,
    override val layoutId: Int = R.layout.item_node_first
) : BaseNodeProvider() {
    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        item as FirstNode
        helper.setText(R.id.tvTitle, item.title)
        ProviderHelper.setArrowSpin(helper, item, false)
    }

    override fun convert(helper: BaseViewHolder, item: BaseNode, payloads: List<Any>) {
        for (payload in payloads) {
            if (payload is Int && payload == EXPAND_COLLAPSE_PAYLOAD) {
                // 增量刷新，使用动画变化箭头
                ProviderHelper.setArrowSpin(helper, item as BaseExpandNode, true)
            }
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        getAdapter()?.expandOrCollapse(
            position = position,
            animate = true,
            notify = true,
            parentPayload = EXPAND_COLLAPSE_PAYLOAD
        )
        data as FirstNode
        data.onClick?.invoke()
    }
}