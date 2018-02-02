package com.farukkaradeniz.sosyalmedyadownloader.model

import com.farukkaradeniz.sosyalmedyadownloader.model.data.Tweet
import io.reactivex.Observable

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
interface TweetRepository: BaseRepository {
    fun getTweet(id: Long): Observable<Tweet>
    fun getGifTweet(id: Long): Observable<List<String>>
}