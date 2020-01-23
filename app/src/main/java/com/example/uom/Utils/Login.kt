package com.example.uom.Utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException

suspend fun getCookieFunc(username: String? ,password: String?): String? {
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
fun login (username: String? ,password: String?): String? {
    val cookie: String? = runBlocking { getCookieFunc(username, password) }
    if (cookie != null) Log.i("Cookie",cookie)
    return cookie
}
