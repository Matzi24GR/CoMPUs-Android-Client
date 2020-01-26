package com.example.uom.utils

import org.jsoup.nodes.Element


fun getTitle(element: Element): String{
    return element.select("a").text()
}
fun getProfs(element: Element): String{
    return element.select("b").text()
}
fun getUrl(element: Element): String{
    return "http://compus.uom.gr/" + element.select("a").attr("href")
}
fun getSemester(element: Element): Int{
    return element.select("i").text().replace("\\D+".toRegex(), "").toInt()
}
fun getActive(element: Element): Boolean{
    return element.select("img").attr("alt").contains("Το μάθημα αυτό είναι ενεργοποιημένο")
}
fun getCode(element: Element): String{
    return "http://compus.uom.gr/" + element.select("a").attr("href").replace("http://compus.uom.gr/".toRegex(), "").replace("/index.php".toRegex(), "")
}
