package com.example.uom.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements_table")
data class Announcement(val announcement: String, val time: Long, val courseString: String) {
    @PrimaryKey
    var id = time.toString()+courseString

    @ColumnInfo(name = "text")
    var text = announcement

    @ColumnInfo(name = "timestamp")
    var timestamp = time

    @ColumnInfo(name = "course")
    var course = courseString
}