package com.example.uom.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException

suspend fun Login(username: String? ,password: String?): String? {
    val url = "https://compus.uom.gr/modules/auth/login.php"
    return GlobalScope.async(Dispatchers.IO) {
        var cookie: String? = null
        try {

            val initial = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .execute()

            cookie = initial.cookie("PHPSESSID")
            val response = Jsoup.connect(url)
                    .data("uname", username)
                    .data("pass", password)
                    .data("login", "") //can be anything
                    .referrer(url)
                    .cookies(initial.cookies())
                    .method(Connection.Method.POST)
                    .execute()


            with(response.body()) {
                cookie = when {
                    contains("Τα Μαθήματά Μου") -> initial.cookie("PHPSESSID")
                    contains("Η Είσοδος Απέτυχε") -> "Wrong_Username/Password"
                    else -> null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (cookie != null) Log.i("Cookie",cookie!!) else Log.i("Cookie","Null Cookie")
        return@async cookie
    }.await()
}