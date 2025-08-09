package com.fphoenixcorneae.baseui

import android.content.Intent

fun <T> starActivity(clazz: Class<T>) {
    BaseUiApp.getInstance()?.currentActivity?.let {
        it.startActivity(Intent(it, clazz))
    }
}