package com.example.uom.Worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.uom.R
import com.example.uom.Repository.AnnouncementRepository
import com.example.uom.Repository.CourseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class AnnouncementWorker(val context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {

    override fun doWork(): Result {
        val repo = AnnouncementRepository(context)

        var builder = NotificationCompat.Builder(context, "announcementChannel")
                .setSmallIcon(R.drawable.ic_refresh_24px)
                .setContentTitle("Ανανέωση Ανακοινώσεων")
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(context).notify(0,builder.build())

        //Refresh Courses
        val CourseJob = GlobalScope.async(Dispatchers.IO) {
            CourseRepository(context).refreshCourses() }
        //Refresh Announcements after Courses
        CourseJob.invokeOnCompletion {
            val job = GlobalScope.async(Dispatchers.IO) {
                repo.refreshAnnouncements()
            }
            job.invokeOnCompletion {
                if (repo.newCount != 0){
                    val list = repo.newAnnouncements
                    for (i in list.indices) {
                        var builder = NotificationCompat.Builder(context, "announcementChannel")
                                .setSmallIcon(R.drawable.ic_refresh_24px)
                                .setContentTitle(list[i].course)
                                .setContentText(list[i].text)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        NotificationManagerCompat.from(context).notify(i,builder.build())
                    }
                }
            }
        }


        return  Result.success()
    }
}