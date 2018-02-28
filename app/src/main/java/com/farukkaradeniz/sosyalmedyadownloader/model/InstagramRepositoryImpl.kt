package com.farukkaradeniz.sosyalmedyadownloader.model

import com.farukkaradeniz.sosyalmedyadownloader.model.data.InstagramPost
import io.reactivex.Observable
import org.json.JSONObject

/**
 * Created by Faruk Karadeniz on 15.02.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
class InstagramRepositoryImpl : InstagramRepository {
    private val errorMessage = "Error no data"

    override fun getInstagramPost(link: String): Observable<InstagramPost> {
        return Observable.create {
            val page = downloadHtmlPage(link)
            try {
                val post = getData(page)
                it.onNext(post)
            } catch (e: Exception) {
                it.onError(e)
            }
            it.onComplete()
        }
    }

    private fun getData(page: String): InstagramPost {
        val jsonRegex = """(<script type="text/javascript">window._sharedData = )(.*)(;</script>)""".toRegex()
        val jsonText = jsonRegex.find(page)?.groupValues?.get(2) ?: ""
        if (jsonText.isEmpty()) {
            throw Throwable(errorMessage)
        }

        val globalJsonObject = JSONObject(jsonText)
        val postData = globalJsonObject.getJSONObject("entry_data")
                .getJSONArray("PostPage")
                .getJSONObject(0)
                .getJSONObject("graphql")
                .getJSONObject("shortcode_media")

        val isVideo = postData.getBoolean("is_video")
        val postType = if (isVideo) {
            "video"
        } else {
            "image"
        }

        val displayResources = postData.getJSONArray("display_resources")
        val image = displayResources.getJSONObject(displayResources.length() - 1)
                .getString("src")
        val videoUrl = if (isVideo) {
            postData.getString("video_url")
        } else {
            null
        }

        val instagramData = InstagramPost(postType, image, videoUrl)
        return instagramData
    }
}