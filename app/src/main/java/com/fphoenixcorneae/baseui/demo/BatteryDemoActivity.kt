package com.fphoenixcorneae.baseui.demo

import android.os.Bundle
import com.fphoenixcorneae.baseui.BaseActivity
import com.fphoenixcorneae.baseui.databinding.ActivityBatteryDemoBinding

class BatteryDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBatteryDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 电池头朝右
        binding.batteryRight1.setLifecycleOwner(this)
        binding.batteryRight2.setPower(0)
        binding.batteryRight3.setPower(20)
        binding.batteryRight4.setPower(40)
        binding.batteryRight5.setPower(100)

        // 电池头朝左
        binding.batteryLeft1.setLifecycleOwner(this)
        binding.batteryLeft2.setPower(0)
        binding.batteryLeft3.setPower(20)
        binding.batteryLeft4.setPower(40)
        binding.batteryLeft5.setPower(100)

        // 电池头朝上
        binding.batteryTop1.setLifecycleOwner(this)

        // 电池头朝下
        binding.batteryBottom1.setLifecycleOwner(this)
    }
}