package com.example.uom.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uom.R
import com.example.uom.Utils.AllTrustingTrustManager
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE)
        if (sharedPreferences.getString("username",null) == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        AllTrustingTrustManager()

        // Finding the Navigation Controller
        val navController = findNavController(R.id.fragNavHost)
        // Setting Navigation Controller with the BottomNavigationView
        bottomNavView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.action_settings) {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}