package com.uminate.devlauncher.activities

import android.os.Bundle

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return super.onNavigateUp()
    }
}