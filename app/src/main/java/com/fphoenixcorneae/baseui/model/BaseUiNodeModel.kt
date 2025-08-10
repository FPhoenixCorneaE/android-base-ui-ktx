package com.fphoenixcorneae.baseui.model

import com.fphoenixcorneae.baseui.demo.BatteryDemoActivity
import com.fphoenixcorneae.baseui.demo.EditTextDemoActivity
import com.fphoenixcorneae.baseui.node.FirstNode
import com.fphoenixcorneae.baseui.node.SecondNode
import com.fphoenixcorneae.baseui.starActivity

object BaseUiNodeModel {
    val list = listOf(
        FirstNode(
            title = "Indicator",
            childNode = mutableListOf(
                SecondNode(
                    childNode = null,
                    title = "HorizontalSlidingIndicator",
                )
            ),
        ),
        FirstNode(
            title = "Battery",
            childNode = null,
            onClick = {
                starActivity(BatteryDemoActivity::class.java)
            }
        ),
        FirstNode(
            title = "EditText",
            childNode = null,
            onClick = {
                starActivity(EditTextDemoActivity::class.java)
            }
        ),
    ).sortedBy { it.title }
}