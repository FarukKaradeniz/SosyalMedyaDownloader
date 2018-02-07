package com.farukkaradeniz.sosyalmedyadownloader.model.data

import java.text.DecimalFormat

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
){
    val videoDuration: String
        get() {
            var sec: Long = duration / 1000
            var min: Long = sec / 60
            sec %= 60
            val hr: Long = min / 60
            min %= 60
            val numberFormat = DecimalFormat("#00")
            val str = "${numberFormat.format(min)}:${numberFormat.format(sec)}"
            return if (hr==0L) {
                str
            }
            else {
                "${numberFormat.format(hr)}:$str"
            }
        }
}

data class TweetMediaVariant(
        val bitrate: Int,
        val mediaUrl: String,
        val type: String
)