package com.example.uom.courses

import androidx.lifecycle.LiveData
import com.example.uom.Database.Course
import com.example.uom.Database.CourseDao

class CourseRepository(private val courseDAO: CourseDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allCourses: LiveData<List<Course>> = courseDAO.getAllCourses()
    suspend fun insertCourse(course: Course) {
        courseDAO.insert(course)
    }
}