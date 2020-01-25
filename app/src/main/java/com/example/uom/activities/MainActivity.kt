package com.example.uom.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.uom.Database.UomDatabase
import com.example.uom.R
import com.example.uom.R.id
import com.example.uom.utils.AllTrustingTrustManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE)
        if (sharedPreferences.getString("username",null) == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        val bottomNavView = findViewById<BottomNavigationView>(id.bottom_navigation)

        AllTrustingTrustManager()

        // Finding the Navigation Controller
        val navController = findNavController(id.fragNavHost)
        // Setting Navigation Controller with the BottomNavigationView
        bottomNavView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        if (id == R.id.action_log_out) {
            GlobalScope.launch(Dispatchers.IO) {
                UomDatabase.getDatabase(this@MainActivity).AnnouncementDAO().deleteAll()
                getSharedPreferences("CREDENTIALS",Context.MODE_PRIVATE).edit().clear().apply()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}