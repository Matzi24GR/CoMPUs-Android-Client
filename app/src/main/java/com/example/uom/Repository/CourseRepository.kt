package com.example.uom.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.uom.Database.Course
import com.example.uom.Database.UomDatabase
import com.example.uom.utils.*

class CourseRepository(private val context: Context) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    private val courseDAO = UomDatabase.getDatabase(context).CourseDao()
    val allCourses: LiveData<List<Course>> = courseDAO.getAllCourses()
    suspend fun insertCourse(course: Course) {
        courseDAO.insert(course)
    }
    suspend fun deleteNotInList(list: List<String>) {
        courseDAO.deleteNotIn(list)
    }

    suspend fun refreshCourses() {
            val document = DocumentFetcher.fetchSite(context,"https://compus.uom.gr/index.php")
            if (document != null && document.html().contains("Τα Μαθήματά Μου")) {
                //Show User Name
                val user = document.select("td[class=info_user]").text()
                context.getSharedPreferences("CREDENTIALS",Context.MODE_PRIVATE).edit().putString("name",user).apply()

                //Parse Courses
                val coursesElements = document.select("td[class=external_table]")
                val arrayList = arrayListOf<String>()
                for (i in coursesElements.indices) {
                    insertCourse(
                            Course(
                                    getTitle(coursesElements[i]),
                                    getProfs(coursesElements[i]),
                                    getUrl(coursesElements[i]),
                                    getSemester(coursesElements[i]),
                                    getActive(coursesElements[i]),
                                    getCode(coursesElements[i])))

                    arrayList.add(getTitle(coursesElements[i]))
                }
                deleteNotInList(arrayList)
            } else {
                val isRefreshed = refreshCookie(context,3)
                if (isRefreshed) refreshCourses()
            }
    }
}