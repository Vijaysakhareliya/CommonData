package com.basecontent.baseActivity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.basecontent.utils.isTabletMode
import com.basecontent.utils.showLog

internal abstract class BaseTabletActivity : AppCompatActivity() {

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        showLog("Orientation : changed")
        if (!isTabletMode()) {
            //Mobile Model
            return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // For Mobile use only portrait mode
        if (!isTabletMode()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}