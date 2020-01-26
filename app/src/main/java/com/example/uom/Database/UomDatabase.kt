package com.example.uom.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Announcement::class,Course::class), version = 5, exportSchema = false)
abstract class UomDatabase : RoomDatabase() {

    abstract fun AnnouncementDAO(): AnnouncementDAO
    abstract fun CourseDao(): CourseDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: UomDatabase? = null

        fun getDatabase(context: Context): UomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        UomDatabase::class.java,"UoM_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}