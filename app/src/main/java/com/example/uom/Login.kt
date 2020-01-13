package com.example.uom

import android.webkit.CookieManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
    var cookie: String? = null
    GlobalScope.launch (Dispatchers.Main) {
        cookie = getCookieFunc(username, password)

    }
    if (cookie != null)
        CookieManager.getInstance().setCookie("http://compus.uom.gr", "PHPSESSID=$cookie")
    return cookie
}