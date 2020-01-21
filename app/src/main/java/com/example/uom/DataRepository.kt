package com.example.uom

import androidx.lifecycle.LiveData
import com.example.uom.Database.Announcement
import com.example.uom.Database.AnnouncementDAO

class DataRepository(private val announcementDAO: AnnouncementDAO) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allAnnouncements: LiveData<List<Announcement>> = announcementDAO.getAllAnnouncements()

    suspend fun insert(announcement: Announcement) {
        announcementDAO.insert(announcement)
    }
}