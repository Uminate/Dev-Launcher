package com.uminate.devlauncher.activities

import android.app.Activity
import android.content.Intent
import android.provider.Settings

abstract class BaseActivity : Activity() {
    protected fun startDevelopmentSettings() = startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
}