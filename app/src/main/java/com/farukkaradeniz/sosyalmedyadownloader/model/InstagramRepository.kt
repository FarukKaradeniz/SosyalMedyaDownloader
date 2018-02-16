package com.farukkaradeniz.sosyalmedyadownloader.model

import com.farukkaradeniz.sosyalmedyadownloader.model.data.InstagramPost
import io.reactivex.Observable

/**
 * Created by Faruk Karadeniz on 15.02.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
interface InstagramRepository: BaseRepository {
    fun getInstagramPost(link: String): Observable<InstagramPost>
}