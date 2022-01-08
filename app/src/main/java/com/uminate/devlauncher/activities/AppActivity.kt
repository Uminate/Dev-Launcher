package com.uminate.devlauncher.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.uminate.devlauncher.R
import com.uminate.devlauncher.ext.AppData


class AppActivity : BaseActivity() {

    private lateinit var appData: AppData
    private val appIcon: ImageView by lazy { findViewById(R.id.app_icon) }
    private val appName: TextView by lazy { findViewById(R.id.app_name) }
    private val packageName: TextView by lazy { findViewById(R.id.package_name) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mPackageName = intent.getStringExtra("packageName")
        if (mPackageName != null) {
            appData = AppData(packageManager, packageManager.getApplicationInfo(mPackageName, 0))
            setContentView(R.layout.activity_app)
            actionBar?.setDisplayHomeAsUpEnabled(true)

            title = appData.name
            appName.text = appData.name
            packageName.text = appData.packageName
            appIcon.setImageDrawable(appData.icon)
        } else finish()
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return super.onNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.developer_button -> startDevelopmentSettings()
            R.id.settings_button -> startDetailsSettingsApp()
            R.id.delete_button -> startDeleteApp()
        }
        return super.onOptionsItemSelected(item)
    }

    fun startSettingsActivity(view: View) = startActivity(Intent(view.context, SettingsActivity::class.java))

    fun startApp(view: View) = appData.startApp(view.context)

    private fun startDetailsSettingsApp() {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.fromParts("package", appData.packageName, null)
        })
    }

    private fun startDeleteApp() {
        startActivity(Intent(Intent.ACTION_DELETE).apply {
            data = Uri.fromParts("package", appData.packageName, null)
        })
    }
}