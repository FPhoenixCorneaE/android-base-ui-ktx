package com.fphoenixcorneae.baseui.demo

import android.os.Bundle
import android.util.Log
import com.fphoenixcorneae.baseui.BaseActivity
import com.fphoenixcorneae.baseui.databinding.ActivityEdittextDemoBinding
import com.fphoenixcorneae.baseui.edittext.PrefixEditText
import com.fphoenixcorneae.baseui.edittext.SuffixEditText

class EditTextDemoActivity : BaseActivity() {

    private lateinit var mViewBinding: ActivityEdittextDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityEdittextDemoBinding.inflate(layoutInflater).also { mViewBinding = it }.root)

        mViewBinding.apply {
            onPrefixTextChanged = object : PrefixEditText.OnTextChanged {
                override fun onTextChanged(text: CharSequence?) {
                    Log.d("Prefix", "setOnTextChanged: $text")
                }
            }
            onSuffixTextChanged = object : SuffixEditText.OnTextChanged {
                override fun onTextChanged(text: CharSequence?) {
                    Log.d("Suffix", "setOnTextChanged: $text")
                }
            }
            // 只允许输入汉字
            regex = listOf("[\u4e00-\u9fa5]+")
            btnPhoneNumber.setOnClickListener {
                etSeparatorPhoneNumber.setText(btnPhoneNumber.text.toString())
            }
            btnBankCard.setOnClickListener {
                etSeparatorBankCard.setText(btnBankCard.text.toString())
            }
        }
    }
}