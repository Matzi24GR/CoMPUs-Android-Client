package com.example.uom.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements_table")
data class Announcement(val announcement: String, val time: Long, val courseString: String) {
    @PrimaryKey
    var id = time.toString()+courseString

    var text = announcement
    var timestamp = time
    var course = courseString
    var isRead = false
}