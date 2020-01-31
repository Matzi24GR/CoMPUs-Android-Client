package com.example.uom.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uom.R
import com.example.uom.SettingsFragment


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, SettingsFragment())
                .commit()
    }
}
