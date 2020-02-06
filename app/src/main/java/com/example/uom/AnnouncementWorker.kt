package com.example.uom

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.uom.announcements.AnnouncementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AnnouncementWorker(val context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {

    override fun doWork(): Result {
        GlobalScope.launch (Dispatchers.IO){AnnouncementRepository(context).refreshAnnouncements()}
        return  Result.success()
    }
}