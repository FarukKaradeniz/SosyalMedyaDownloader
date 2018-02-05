package com.farukkaradeniz.sosyalmedyadownloader.ui.fragments

import com.farukkaradeniz.sosyalmedyadownloader.model.data.Tweet

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
interface DetailView {
    fun updateTwitterUI(tweet: Tweet, list: List<String?>)
    fun updateGifUI(list: List<String>)
    fun hideProgressBar()
    fun showProgressBar()
    fun showToast(msg: String)
}