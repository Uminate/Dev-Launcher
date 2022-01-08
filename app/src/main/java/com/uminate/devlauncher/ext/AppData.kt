package com.uminate.devlauncher.ext

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

data class AppData(val packageManager: PackageManager, val info: ApplicationInfo) {
    val packageName get() = info.packageName
    val name: String by lazy { packageManager.getApplicationLabel(info).toString() }
    val icon: Drawable by lazy { packageManager.getApplicationIcon(info) }

    fun startApp(context: Context) = context.startActivity(context.packageManager.getLaunchIntentForPackage(packageName))
}
