package com.example.uom.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AnnouncementDAO {

    @Query("Select * from announcements_table ORDER BY time DESC")
    fun getAllAnnouncements(): LiveData<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(announcement: Announcement):Long

    @Query("DELETE FROM announcements_table")
    suspend fun deleteAll()

    @Update
    suspend fun  updateAnnouncement(announcement: Announcement)

    @Query("SELECT COUNT(id) FROM ANNOUNCEMENTS_TABLE WHERE isRead = 0")
    fun getUnreadCount(): LiveData<Int>

    @Query("Select * from announcements_table WHERE id == :i ORDER BY time DESC")
    fun getAnnouncementFromId(i:Long): Announcement
}