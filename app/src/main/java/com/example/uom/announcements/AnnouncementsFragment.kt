package com.example.uom.announcements


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
import com.example.uom.Database.Announcement
import com.example.uom.Database.Course
import com.example.uom.Database.UomDatabase
import com.example.uom.R
import kotlinx.coroutines.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


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
        announcementViewModel.allAnnouncements.observe(this, Observer { announcements -> announcements?.let { adapter.setAnnouncements(it) } })

        val CourseDao = UomDatabase.getDatabase(this.requireContext()).CourseDao()

        suspend fun getAnnouncements(cookie: String, courses: List<Course>, id: Int): Document? {
            return GlobalScope.async(Dispatchers.IO) {
                var document: Document? = null
                try {
                    Jsoup.connect(courses[id].Url)
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


        loginButton.setOnClickListener {
            progressBar.isIndeterminate = true
            progressBar.visibility = View.VISIBLE

            val job = GlobalScope.launch(Dispatchers.IO) {

                val courses: List<Course> = CourseDao.getAllCoursesStatic()
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

                        //announcement TIME selection
                        val dateS = "\\d\\d.\\d\\d.\\d\\d\\d\\d \\d\\d:\\d\\d".toRegex().find(text)!!.value
                        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                        val date = dateFormat.parse(dateS)!!

                        text = text.replace("\\d\\d.\\d\\d.\\d\\d\\d\\d \\d\\d:\\d\\d ".toRegex(), "")

                        //Add to database
                        announcementViewModel.insert(Announcement(text, date.time, courses[i].Title))
                    }
                    progressBar.incrementProgressBy(1)
                }
            }
            job.invokeOnCompletion {GlobalScope.launch(Dispatchers.Main) { progressBar.visibility = View.GONE }}
        }


        return view
    }
}
