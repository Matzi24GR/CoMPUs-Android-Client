package com.example.uom.Announcements


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uom.Courses.Course
import com.example.uom.Database.Announcement
import com.example.uom.Database.UomDatabase
import com.example.uom.R
import kotlinx.coroutines.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AnnouncementsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_announcements, container, false)

        val userText = getActivity()!!.findViewById<TextView>(R.id.user_text)
        val loginButton = getActivity()!!.findViewById<Button>(R.id.login_button)
        val progressBar = activity!!.findViewById<ProgressBar>(R.id.progress_bar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.announcementsRecyclerView)

        val cookie = activity!!.getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE).getString("cookie", null)

        val adapter = AnnouncementsListAdapter(this.context!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)


        val announcementViewModel = ViewModelProviders.of(this)[AnnouncementViewModel::class.java]

        announcementViewModel.allAnnouncements.observe(this, Observer { announcements ->
            announcements?.let { adapter.setAnnouncements(it) }
        })

        val dao = UomDatabase.getDatabase(this.requireContext()).AnnouncementDAO()
        //GlobalScope.launch { dao.deleteAll() }

        suspend fun getAnnouncements(cookie: String, courses: ArrayList<Course>, id: Int): Document? {
            return GlobalScope.async(Dispatchers.IO) {
                var document: Document? = null
                try {
                    Jsoup.connect(courses[id].url)
                            .cookie("PHPSESSID", cookie)
                            .method(Connection.Method.GET)
                            .execute()
                    delay(100)
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

        suspend fun fetchHome(cookie: String?): Document? {
            val url = "https://compus.uom.gr/index.php"
            var document: Document? = null
            return GlobalScope.async(Dispatchers.IO) {
                try {
                    val response = Jsoup.connect(url)
                            .cookie("PHPSESSID",cookie)
                            .method(Connection.Method.GET)
                            .execute()
                    document = response.parse()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return@async document
            }.await()
        }

        fun showHome(document: Document): ArrayList<Course> {
            //Show User Name
            val user = document.select("td[class=info_user]").text()
            userText.text = user
            //Parse Courses
            val courses = ArrayList<Course>()
            val coursesElements = document.select("td[class=external_table]")
            for (i in coursesElements.indices) courses.add(Course(coursesElements[i]))

            return courses
        }

        loginButton.setOnClickListener {
            progressBar.isIndeterminate = true
            progressBar.visibility = View.VISIBLE

            GlobalScope.launch(Dispatchers.Main) {

                val document = fetchHome(cookie)
                if (document != null) {
                    val courses = showHome(document)
                    progressBar.isIndeterminate = false
                    progressBar.progress = 0
                    progressBar.max = courses.size

                    for (i in 0 until courses.size) {
                        var document2 = getAnnouncements(cookie!!, courses, i) ?: continue
                        var html = document2.html()
                        html = html.replace("<br>", "!@#$")
                        document2 = Jsoup.parse(html)
                        val announcementElements = document2.select("div")
                        for (j in announcementElements.indices) {
                            var text: String = announcementElements[j].text().replace("!@#$", "\n").replace("\\d\\d.\\d\\d.\\d\\d\\d\\d\\n\\d\\d:\\d\\d Τελευταία Ενημέρωση:".toRegex(), "")
                            val dateS = "\\d\\d.\\d\\d.\\d\\d\\d\\d \\d\\d:\\d\\d".toRegex().find(text)!!.value

                            text = text.replace("\\d\\d.\\d\\d.\\d\\d\\d\\d \\d\\d:\\d\\d ".toRegex(), "")
                            val complete = dateS + " - " + courses[i].title + "\n\n" + text
                            adapter.notifyDataSetChanged()

                            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                            val date = dateFormat.parse(dateS)!!
                            announcementViewModel.insert(Announcement(complete, date.time, courses[i].title))
                        }
                        progressBar.incrementProgressBy(1)
                    }
                    progressBar.visibility = View.GONE
                }
            }
        }


        return view
    }
}
