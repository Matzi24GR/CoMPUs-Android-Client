package com.example.uom.announcements

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.uom.DataRepository
import com.example.uom.Database.Announcement
import com.example.uom.Database.UomDatabase
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.
class AnnouncementViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: DataRepository
    // LiveData gives us updated announcements when they change.
    val allAnnouncements: LiveData<List<Announcement>>

    init {
        // Gets reference to AnnouncementsDAO from UomDatabase to construct
        // the correct DataRepository.
        val announcementDAO = UomDatabase.getDatabase(application).AnnouncementDAO()
        repository = DataRepository(announcementDAO)
        allAnnouncements = repository.allAnnouncements
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(announcement: Announcement) = viewModelScope.launch {
        repository.insert(announcement)
    }
}