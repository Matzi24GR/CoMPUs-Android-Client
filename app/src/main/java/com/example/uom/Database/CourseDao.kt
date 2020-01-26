package com.example.uom.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CourseDao {

    @Query("Select * from courses_table")
    fun getAllCourses(): LiveData<List<Course>>

    @Query("Select * from courses_table")
    fun getAllCoursesStatic(): List<Course>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: Course)

    @Query("DELETE FROM courses_table")
    suspend fun deleteAll()
}