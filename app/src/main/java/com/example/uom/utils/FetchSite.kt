package com.example.uom.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

suspend fun fetchSite(context: Context, url: String): Document? {
    var document: Document? = null
    val cookie = context.getSharedPreferences("CREDENTIALS",Context.MODE_PRIVATE).getString("cookie",null)
    document = GlobalScope.async(Dispatchers.IO) {
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
    return document
}