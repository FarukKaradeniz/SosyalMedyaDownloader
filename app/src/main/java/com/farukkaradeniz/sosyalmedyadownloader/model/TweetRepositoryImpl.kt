package com.farukkaradeniz.sosyalmedyadownloader.model

import android.util.Log
import com.farukkaradeniz.sosyalmedyadownloader.api.TwitterApi
import com.farukkaradeniz.sosyalmedyadownloader.model.data.Tweet
import com.farukkaradeniz.sosyalmedyadownloader.model.data.TweetMediaVariant
import io.reactivex.Observable
import java.net.URL

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
class TweetRepositoryImpl(key: String, secret: String): TweetRepository {
    private val twitterApi: TwitterApi = TwitterApi(key, secret)


    override fun getTweet(id: Long): Observable<Tweet> {
        return Observable.create {
            e ->
            try {
                val status = twitterApi.getTweetStatus(id)
                status.extendedMediaEntities.forEach {
                    val type = it.type
                    val duration = it.videoDurationMillis
                    val url = it.mediaURLHttps
                    val mediaVariants = mutableListOf<TweetMediaVariant>()
                    it.videoVariants.forEach {
                        val bitrate = it.bitrate
                        val mediaUrl = it.url
                        val contentType = it.contentType
                        val mediaVariant = TweetMediaVariant(bitrate, mediaUrl, contentType)
                        mediaVariants.add(mediaVariant)
                    }
                    val tweet = Tweet(status.id, type, duration, url, mediaVariants)
                    e.onNext(tweet)
                }
                if (status.extendedMediaEntities.isEmpty()) {
                    e.onNext(Tweet(status.id, "Gif", -1, "zz", emptyList()))
                }
            }
            catch (error: Exception) {
                Log.d(javaClass.simpleName, error.message)
                e.onError(Throwable("Input is not valid."))
            }
            e.onComplete()
        }
    }

    private fun downloadHtmlPage(url: String): String {
        try {
            val connection = URL(url).openConnection()
            return connection.getInputStream().bufferedReader().readText()
        }catch (e: Exception) {
            println(e.message)
            return ""
        }
    }

    override fun getGifTweet(id: Long): Observable<List<String>> {
        return Observable.create {
            val htmlpage = downloadHtmlPage("https://twitter.com/i/videos/$id")
            if (htmlpage.isEmpty())
                it.onError(Throwable("Error retrieving the data"))
            val regex = """([A-Za-z0-9_-]+)\.mp4""".toRegex()
            val mediaId = regex.find(htmlpage)?.groupValues?.get(1) //ID'nin değeri
            it.onNext(listOf("https://pbs.twimg.com/tweet_video_thumb/$mediaId.jpg",
                    "https://video.twimg.com/tweet_video/$mediaId.mp4"))
            it.onComplete()
        }
    }

}