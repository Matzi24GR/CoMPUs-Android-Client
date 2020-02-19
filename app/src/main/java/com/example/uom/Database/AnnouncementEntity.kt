package com.example.uom.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements_table")
data class Announcement(val text: String, val time: Long, val course: String) {
    @PrimaryKey
    var id = time.toString()+course
    var isRead = false
}