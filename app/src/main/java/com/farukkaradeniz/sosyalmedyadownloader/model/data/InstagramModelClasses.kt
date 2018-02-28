package com.farukkaradeniz.sosyalmedyadownloader.model.data

/**
 * Created by Faruk Karadeniz on 15.02.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
class InstagramPost(
        val type: String,
        val previewImageUrl: String,
        val videoUrl: String?,
        val duration: String = "--/--"
)