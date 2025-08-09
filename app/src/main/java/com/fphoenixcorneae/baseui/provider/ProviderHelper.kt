package com.fphoenixcorneae.baseui.provider

import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fphoenixcorneae.baseui.R

object ProviderHelper {
    fun setArrowSpin(helper: BaseViewHolder, data: BaseExpandNode, isAnimate: Boolean) {
        val imageView = helper.getView<ImageView>(R.id.ivArrow)
        if (data.isExpanded) {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(90f)
                    .start()
            } else {
                imageView.rotation = 90f
            }
        } else {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(0f)
                    .start()
            } else {
                imageView.rotation = 0f
            }
        }
    }
}