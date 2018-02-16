package com.farukkaradeniz.sosyalmedyadownloader.presenters

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
interface DetailPresenter {
    fun loadMedia(link: String)
    fun extractTwitterId(link: String): Long
    fun loadGifLinks(id: Long)
    fun downloadMedia(link: String, mediaExtension: String)
}