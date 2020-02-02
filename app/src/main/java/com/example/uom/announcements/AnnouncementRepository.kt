package com.example.uom.announcements

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.uom.Database.Announcement
import com.example.uom.Database.AnnouncementDAO
import com.example.uom.Database.Course
import com.example.uom.Database.UomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AnnouncementRepository(val context: Context) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val announcementDAO = UomDatabase.getDatabase(context).AnnouncementDAO()
    val allAnnouncements: LiveData<List<Announcement>> = announcementDAO.getAllAnnouncements()
    suspend fun insertAnnouncement(announcement: Announcement) {
        announcementDAO.insert(announcement)
    }

    suspend fun refreshAnnouncements() {
        val courses: List<Course> = UomDatabase.getDatabase(context).CourseDao().getAllCoursesStatic()

        val cookie = context.getSharedPreferences("CREDENTIALS",Context.MODE_PRIVATE).getString("cookie",null)

        for (i in 0 until courses.size) {
            var document2 = getAnnouncements(cookie!!, courses, i) ?: continue
            var html = document2.html()
            html = html.replace("<br>", "!@#$")
            document2 = Jsoup.parse(html)
            val announcementElements = document2.select("div")
            for (j in announcementElements.indices) {

                var text: String = announcementElements[j].text().replace("!@#$", "\n").replace("\\d\\d.\\d\\d.\\d\\d\\d\\d\\n\\d\\d:\\d\\d Τελευταία Ενημέρωση:".toRegex(), "")

                //announcement TIME selection
                val dateS = "\\d\\d.\\d\\d.\\d\\d\\d\\d \\d\\d:\\d\\d".toRegex().find(text)!!.value
                val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                val date = dateFormat.parse(dateS)!!

                text = text.replace("\\d\\d.\\d\\d.\\d\\d\\d\\d \\d\\d:\\d\\d ".toRegex(), "")

                //Add to database
                insertAnnouncement(Announcement(text, date.time, courses[i].Title))
            }
        }
    }

    private suspend fun getAnnouncements(cookie: String, courses: List<Course>, id: Int): Document? {
        return GlobalScope.async(Dispatchers.IO) {
            var document: Document? = null
            try {
                Jsoup.connect(courses[id].Url)
                        .cookie("PHPSESSID", cookie)
                        .method(Connection.Method.GET)
                        .execute()
                delay((100..300).random().toLong())
                val response = Jsoup.connect("http://compus.uom.gr/modules/anns/anns.php")
                        .cookie("PHPSESSID", cookie)
                        .method(Connection.Method.GET)
                        .execute()
                document = response.parse()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return@async document
        }.await()
    }
}