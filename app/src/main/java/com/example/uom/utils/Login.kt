package com.example.uom.utils

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException

private suspend fun getCookie(username: String?, password: String?): String? {
    val url = "https://compus.uom.gr/modules/auth/login.php"
    return GlobalScope.async(Dispatchers.IO) {
        var cookie: String? = null
        try {

            val initial = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .execute()
            delay(100)

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

suspend fun login(context: Context) {
    val sharedPreferences = context.getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE)

    val cookie :String? = getCookie(
            sharedPreferences.getString("username",null),
            sharedPreferences.getString("password",null)
    )
    when (cookie) {
        "Wrong_Username/Password" -> sharedPreferences.edit().putString("status","wrong").apply()
        null -> sharedPreferences.edit().putString("status","error").apply()
        else -> {
            sharedPreferences.edit().putString("status","success").apply()
            sharedPreferences.edit().putString("cookie",cookie).apply()
        }
    }

}