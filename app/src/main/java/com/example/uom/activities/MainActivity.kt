package com.example.uom.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import com.example.uom.Database.UomDatabase
import com.example.uom.R
import com.example.uom.announcements.AnnouncementRepository
import com.example.uom.announcements.AnnouncementWorker
import com.example.uom.courses.CourseRepository
import com.example.uom.utils.AllTrustingTrustManager
import com.example.uom.utils.refreshSecondaryCookie
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AllTrustingTrustManager()

        // Finding the Navigation Controller
        val navController = findNavController(R.id.fragNavHost)
        // Setting Navigation Controller with the BottomNavigationView
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavView.setupWithNavController(navController)
        val menuItemId: Int = bottomNavView.getMenu().getItem(1).getItemId()
        val badge: BadgeDrawable = bottomNavView.getOrCreateBadge(menuItemId)
        val unreadCount = AnnouncementRepository(this).unreadCount
        unreadCount.observe(this, Observer {
            val countval = unreadCount.value!!
            badge.isVisible = countval != 0
            badge.number = countval}
        )


        val sharedPreferences = getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE)

        if (sharedPreferences.getString("status",null) != "success") {

            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)

        } else {

            val fullName = sharedPreferences.getString("name",null)
            val CourseJob = GlobalScope.async(Dispatchers.IO) {
                CourseRepository(this@MainActivity).refreshCourses() }
            CourseJob.invokeOnCompletion {
                GlobalScope.launch(Dispatchers.IO) {
                    AnnouncementRepository(this@MainActivity).refreshAnnouncements() }}

        }

        GlobalScope.launch { refreshSecondaryCookie(this@MainActivity,3) }

        //Work Manager Sync for announcements
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val announcementRefreshWork = PeriodicWorkRequestBuilder<AnnouncementWorker>(
                30,TimeUnit.MINUTES,
                15,TimeUnit.MINUTES
        ).setConstraints(constraints).build()
        //WorkManager.getInstance(this).enqueue(announcementRefreshWork)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_log_out) {
            GlobalScope.launch(Dispatchers.IO) {
                UomDatabase.getDatabase(this@MainActivity).AnnouncementDAO().deleteAll()
                UomDatabase.getDatabase(this@MainActivity).CourseDao().deleteAll()
                getSharedPreferences("CREDENTIALS",Context.MODE_PRIVATE).edit().clear().apply()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        } else if (id == R.id.action_refresh) {
            GlobalScope.launch(Dispatchers.IO) {
                CourseRepository(this@MainActivity).refreshCourses()
                AnnouncementRepository(this@MainActivity).refreshAnnouncements()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}