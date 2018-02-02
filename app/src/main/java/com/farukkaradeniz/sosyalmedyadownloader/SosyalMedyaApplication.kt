package com.farukkaradeniz.sosyalmedyadownloader

import android.app.Application
import com.downloader.PRDownloader

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
class SosyalMedyaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        PRDownloader.initialize(this)
    }
}