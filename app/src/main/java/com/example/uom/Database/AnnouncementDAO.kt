package com.example.uom.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnnouncementDAO {

    @Query("Select * from announcements_table ORDER BY timestamp DESC")
    fun getAllAnnouncements(): LiveData<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(announcement: Announcement)

    @Query("DELETE FROM announcements_table")
    suspend fun deleteAll()
}