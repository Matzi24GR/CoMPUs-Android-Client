package com.example.uom.Worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.uom.Repository.AnnouncementRepository
import com.example.uom.Repository.CourseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AnnouncementWorker(val context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {

    override fun doWork(): Result {
        //Refresh Courses
        val CourseJob = GlobalScope.async(Dispatchers.IO) {
            CourseRepository(context).refreshCourses() }
        //Refresh Announcements after Courses
        CourseJob.invokeOnCompletion {
            GlobalScope.launch(Dispatchers.IO) {
                AnnouncementRepository(context).refreshAnnouncements() }}
        return  Result.success()
    }
}