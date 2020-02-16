package com.example.uom.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.uom.Repository.CourseRepository
import com.example.uom.Database.Course
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.
class CourseViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: CourseRepository
    // LiveData gives us updated announcements when they change.
    val allCourses: LiveData<List<Course>>

    init {
        // Gets reference to AnnouncementsDAO from UomDatabase to construct
        // the correct DataRepository.
        repository = CourseRepository(this.getApplication())
        allCourses = repository.allCourses
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(course: Course) = viewModelScope.launch {
        repository.insertCourse(course)
    }
}