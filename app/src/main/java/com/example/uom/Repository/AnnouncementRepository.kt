package com.example.uom.Repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.uom.Database.Announcement
import com.example.uom.Database.Course
import com.example.uom.Database.UomDatabase
import com.example.uom.utils.DocumentFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AnnouncementRepository(val context: Context) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    private val announcementDAO = UomDatabase.getDatabase(context).AnnouncementDAO()
    val allAnnouncements: LiveData<List<Announcement>> = announcementDAO.getAllAnnouncements()
    private val newIdsArrayList: ArrayList<Long> = arrayListOf()
    val newAnnouncements: List<Announcement> = getAnnouncementsByIds(newIdsArrayList)
    val newCount = newIdsArrayList.size
    val unreadCount: LiveData<Int> = announcementDAO.getUnreadCount()

    fun getAnnouncementsByIds(arrayList: ArrayList<Long>):List<Announcement>{
        val list = mutableListOf<Announcement>()
        for (i in arrayList.indices)
            list[i] = announcementDAO.getAnnouncementFromId(arrayList[i])
        return list
    }

    suspend fun insertAnnouncement(announcement: Announcement):Long {
        return announcementDAO.insert(announcement)
    }

    suspend fun refreshAnnouncements() {
        newIdsArrayList.clear()
        val courses: List<Course> = UomDatabase.getDatabase(context).CourseDao().getAllCoursesStatic()

        val cookie = context.getSharedPreferences("CREDENTIALS",Context.MODE_PRIVATE).getString("cookie",null)

        for (i in courses.indices) {
            var document2 = DocumentFetcher.getAnnouncements(cookie!!,courses[i]) ?: continue
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
                val  id: Long = insertAnnouncement(Announcement(text, date.time, courses[i].Title))
                if (id != -1L) {
                    newIdsArrayList.add(id)
                }
            }
        }
    }

    fun setRead(announcement: Announcement, boolean: Boolean) {
        announcement.isRead = boolean
        GlobalScope.launch(Dispatchers.IO) {announcementDAO.updateAnnouncement(announcement)}
    }

    private suspend fun getAnnouncements(cookie: String, courses: List<Course>, id: Int): Document? {
        Log.i("AnnouncementRepository","getting "+courses[id].titleStr+" announcements")
        return GlobalScope.async(Dispatchers.IO) {
            var document: Document? = null
            try {
                //delay(10)
                Jsoup.connect(courses[id].Url)
                        .cookie("PHPSESSID", cookie)
                        .method(Connection.Method.GET)
                        .execute()
                //delay(10)
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
