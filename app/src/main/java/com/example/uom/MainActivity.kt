package com.example.uom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        AllTrustingTrustManager()
        fun setupViews() {
            // Finding the Navigation Controller
            var navController = findNavController(R.id.fragNavHost)

            // Setting Navigation Controller with the BottomNavigationView
            bottomNavView.setupWithNavController(navController)
        }
        setupViews()
    }
}