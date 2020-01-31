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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uom.Database.Course
import com.example.uom.R
import com.example.uom.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.nodes.Document

class AllCoursesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_all_courses, container, false)

        val userText = activity!!.findViewById<TextView>(R.id.user_text)
        val loginButton = activity!!.findViewById<Button>(R.id.login_button)
        val progressBar = activity!!.findViewById<ProgressBar>(R.id.progress_bar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.courses_recycler_view)

        val cookie = activity!!.getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE).getString("cookie", null)

        val adapter = CourseListAdapter(this.context!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val courseViewModel = ViewModelProvider(this).get(CourseViewModel::class.java)
        courseViewModel.allCourses.observe(this, Observer { courses -> courses?.let {adapter.setCourses(it)} })

        fun showHome(document: Document) {
            //Show User Name
            val user = document.select("td[class=info_user]").text()
            context!!.getSharedPreferences("CREDENTIALS",Context.MODE_PRIVATE).edit().putString("name",user).apply()
            userText.text = user

            //Parse Courses
            val coursesElements = document.select("td[class=external_table]")
            for (i in coursesElements.indices)
                courseViewModel.insert(
                    Course(
                            getTitle(coursesElements[i]),
                            getProfs(coursesElements[i]),
                            getUrl(coursesElements[i]),
                            getSemester(coursesElements[i]),
                            getActive(coursesElements[i]),
                            getCode(coursesElements[i])))
        }

        loginButton.setOnClickListener {

            GlobalScope.launch  (Dispatchers.Main) {
                progressBar.isIndeterminate = true
                progressBar.visibility = View.VISIBLE

                val document = fetchSite(context!!,"https://compus.uom.gr/index.php")
                if (document != null) {
                    showHome(document)
                }
                progressBar.visibility = View.GONE
            }

        }
        // Inflate the layout for this fragment
        return view
    }


}
