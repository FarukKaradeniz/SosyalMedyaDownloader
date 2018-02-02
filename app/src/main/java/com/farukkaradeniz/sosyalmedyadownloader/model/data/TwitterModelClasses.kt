package com.farukkaradeniz.sosyalmedyadownloader.model.data

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
data class Tweet(
        val id: Long,
        val type: String,
        val duration: Long,
        val mediaUrl: String,
        val mediaVariant: List<TweetMediaVariant>
)

data class TweetMediaVariant(
        val bitrate: Int,
        val mediaUrl: String,
        val type: String
)