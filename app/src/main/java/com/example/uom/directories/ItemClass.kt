package com.example.uom.directories

import org.jsoup.nodes.Element

class ItemClass(element: Element){
    val name = element.select("td[valign=\"center\"] span").text()
    val url = element.select("td[valign=\"center\"] a").attr("abs:href")
    val imgsrc = element.select("td[valign=\"center\"] img").attr("src")
    val isDirectory = imgsrc.contains("dossier")
}