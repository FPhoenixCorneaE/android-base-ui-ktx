package com.fphoenixcorneae.baseui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fphoenixcorneae.baseui.databinding.ActivityMainBinding
import com.fphoenixcorneae.baseui.model.BaseUiNodeModel

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvBaseUiNode.apply {
            adapter = BaseUiNodeAdapter().apply {
                setList(BaseUiNodeModel.list)
            }
            setHasFixedSize(true)
            setItemViewCacheSize(200)
        }
    }
}