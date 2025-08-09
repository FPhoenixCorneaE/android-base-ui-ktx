package com.fphoenixcorneae.baseui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class BaseUiApp : Application() {

    companion object {
        private var sInstance: BaseUiApp? = null

        fun getInstance(): BaseUiApp? {
            return sInstance
        }
    }

    var currentActivity: AppCompatActivity? = null

    override fun onCreate() {
        super.onCreate()
        sInstance = this

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = activity as? AppCompatActivity
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }
        })
    }
}