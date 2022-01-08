package com.uminate.devlauncher.activities

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uminate.devlauncher.R
import com.uminate.devlauncher.ext.AppData


class MainActivity : BaseActivity() {
    inner class AppListAdapter internal constructor(private var appList: List<AppData>) : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
            private val appName: TextView = itemView.findViewById(R.id.itemName)
            private val packageName: TextView = itemView.findViewById(R.id.itemPackageName)
            private val appIcon: ImageView = itemView.findViewById(R.id.itemLogo)

            var appData: AppData? = null
                set(value) {
                    field = value
                    appName.text = value?.name
                    packageName.text = value?.packageName
                    appIcon.contentDescription = value?.name
                    appIcon.setImageDrawable(value?.icon)
                }

            override fun onClick(view: View) = startActivity(Intent(this@MainActivity, AppActivity::class.java).putExtra("packageName", appData?.packageName))

            override fun onLongClick(view: View): Boolean {
                appData?.startApp(view.context)
                return true
            }

            init {
                itemView.setOnClickListener(this)
                itemView.setOnLongClickListener(this)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_item_view, parent, false))

        override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
            viewHolder.appData = appList[index]
        }

        override fun getItemCount(): Int = appList.size

        fun setAppList(value: List<AppData>) {
            appList = value
            notifyDataSetChanged()
        }
    }

    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = AppListAdapter(refreshApps())
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.developer_button -> startDevelopmentSettings()
            R.id.refresh_button -> (recyclerView.adapter as AppListAdapter).setAppList(refreshApps())
            R.id.launcher_button -> resetPreferredLauncherAndOpenChooser()
            R.id.settings_button -> startSettingsActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshApps(): List<AppData> = packageManager.getInstalledApplications(PackageManager.GET_META_DATA).filter {
        (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0
    }.map { AppData(packageManager, it) }

    private fun resetPreferredLauncherAndOpenChooser() {
        val componentName = ComponentName(this, Activity::class.java)
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        startActivity(Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP)
    }

    private fun startSettingsActivity() = startActivity(Intent(this, SettingsActivity::class.java))

    override fun onBackPressed() {}
}