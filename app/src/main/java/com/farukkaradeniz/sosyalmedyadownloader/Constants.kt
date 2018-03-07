package com.farukkaradeniz.sosyalmedyadownloader

import android.os.Environment

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
class Constants {
    companion object {
        val TWITTER = "twitter"
        val INSTAGRAM = "instagram"
        val DOWNLOAD_DIRECTORY =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val NAME = "name"
        val LINK = "link"
    }
}