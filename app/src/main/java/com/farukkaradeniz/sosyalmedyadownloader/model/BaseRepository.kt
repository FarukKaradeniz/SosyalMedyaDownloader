package com.farukkaradeniz.sosyalmedyadownloader.model

import java.net.URL

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
interface BaseRepository {
    fun downloadHtmlPage(url: String): String {
        return try {
            val connection = URL(url).openConnection()
            connection.getInputStream().bufferedReader().readText()
        }catch (e: Exception) {
            println(e.message)
            ""
        }
    }
}