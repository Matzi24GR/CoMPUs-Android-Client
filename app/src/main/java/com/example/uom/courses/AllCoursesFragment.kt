package com.example.uom.courses


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uom.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class AllCoursesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_all_courses, container, false)

        val userText = getActivity()!!.findViewById<TextView>(R.id.user_text)
        val loginButton = getActivity()!!.findViewById<Button>(R.id.login_button)
        val progressBar = getActivity()!!.findViewById<ProgressBar>(R.id.progress_bar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view2)

        val cookie = activity!!.getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE).getString("cookie", null)

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

            //Display Courses
            val adapter = CourseAdapter(context, R.layout.course_item, courses)
            val linearLayoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter

            return courses
        }

        loginButton.setOnClickListener {

            GlobalScope.launch  (Dispatchers.Main) {
                progressBar.isIndeterminate = true
                progressBar.visibility = View.VISIBLE

                val document = fetchHome(cookie)
                if (document != null) {
                    val courses = showHome(document)
                }
                progressBar.visibility = View.GONE
            }

        }





        // Inflate the layout for this fragment
        return view
    }


}
