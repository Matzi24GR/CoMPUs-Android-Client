package com.example.uom.utils

import android.content.Context
import android.util.Log
import com.example.uom.Database.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

object DocumentFetcher {

    var inUse = false

    suspend fun fetchSite(context: Context, url: String): Document? {
        if (inUse) {
            delay(500)
            return fetchSite(context, url)
        }
        inUse = true
        var document: Document? = null
        val cookie = context.getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE).getString("cookie", null)
        document = GlobalScope.async(Dispatchers.IO) {
            try {
                val response = Jsoup.connect(url)
                        .cookie("PHPSESSID", cookie)
                        .method(Connection.Method.GET)
                        .execute()
                document = response.parse()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return@async document
        }.await()
        inUse = false
        return document
    }

    suspend fun getAnnouncements(cookie: String, course: Course): Document? {
        if (inUse) {
            delay(1000)
            return getAnnouncements(cookie, course)
        }
        inUse = true
        Log.i("AnnouncementRepository","getting "+course.titleStr+" announcements")
        return GlobalScope.async(Dispatchers.IO) {
            var document: Document? = null
            try {
                Jsoup.connect(course.Url)
                        .cookie("PHPSESSID", cookie)
                        .method(Connection.Method.GET)
                        .execute()
                val response = Jsoup.connect("http://compus.uom.gr/modules/anns/anns.php")
                        .cookie("PHPSESSID", cookie)
                        .method(Connection.Method.GET)
                        .execute()
                document = response.parse()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            inUse= false
            return@async document
        }.await()

    }
}