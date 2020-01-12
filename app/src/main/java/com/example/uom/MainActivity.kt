package com.example.uom

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.CookieManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usrText = findViewById<EditText>(R.id.username_textview)
        val passwdText = findViewById<EditText>(R.id.password_textview)
        val userText = findViewById<TextView>(R.id.user_text)
        val loginButton = findViewById<Button>(R.id.login_button)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        //val webView = findViewById<WebView>(R.id.webView)

        AllTrustingTrustManager()

        suspend fun login(username: String? ,password: String?): String? {
            val url = "https://compus.uom.gr/modules/auth/login.php"
            return GlobalScope.async(Dispatchers.IO) {
                var cookie: String? = null
                try {
                    val response = Jsoup.connect(url)
                            .data("uname", username)
                            .data("pass", password)
                            .data("login", "submit")
                            .method(Connection.Method.POST)
                            .execute()

                    cookie = if (response.body().contains("Τα Μαθήματά Μου")) response.cookie("PHPSESSID") else null
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return@async cookie
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

        fun showHome(document: Document) {
            //Show User Name
            val user = document.select("td[class=info_user]").text()
            userText.text = user

            //Parse Courses
            val courses = ArrayList<Course>()
            val coursesElements = document.select("td[class=external_table]")
            for (i in coursesElements.indices) courses.add(Course(coursesElements[i]))

            //Display Courses
            val adapter = CourseAdapter(applicationContext, R.layout.course_item, courses)
            val linearLayoutManager = LinearLayoutManager(applicationContext)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        }


        loginButton.setOnClickListener {
            GlobalScope.launch  (Dispatchers.Main) {
                val username = usrText.text.toString()
                val password = passwdText.text.toString()
                if (username.contains("42069"))
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/dQw4w9WgXcQ")))

                val cookie = login(username,password)
                if (cookie != null) {
                    CookieManager.getInstance().setCookie("http://compus.uom.gr","PHPSESSID=$cookie")
                    val document = fetchHome(cookie)
                    if (document != null)
                        showHome(document)
                } else
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}