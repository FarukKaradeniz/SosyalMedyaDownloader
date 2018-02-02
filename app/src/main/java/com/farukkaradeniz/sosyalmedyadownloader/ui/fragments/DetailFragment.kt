package com.farukkaradeniz.sosyalmedyadownloader.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.farukkaradeniz.sosyalmedyadownloader.R
import com.farukkaradeniz.sosyalmedyadownloader.model.TweetRepositoryImpl
import com.farukkaradeniz.sosyalmedyadownloader.model.data.Tweet
import com.farukkaradeniz.sosyalmedyadownloader.model.data.TweetMediaVariant
import com.farukkaradeniz.sosyalmedyadownloader.presenters.DetailPresenter
import com.farukkaradeniz.sosyalmedyadownloader.presenters.DetailPresenterImpl
import com.farukkaradeniz.sosyalmedyadownloader.setImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
class DetailFragment : Fragment(), DetailView {
    private var mediaURL: String? = null
    private var website: String? = null
    private lateinit var presenter: DetailPresenter
    private lateinit var mediaList: List<TweetMediaVariant>

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(website: String, mediaURL: String): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putString(ARG_PARAM2, website)
            args.putString(ARG_PARAM1, mediaURL)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            website = arguments?.getString(ARG_PARAM2)
            mediaURL = arguments?.getString(ARG_PARAM1)
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val key: String = context.getString(R.string.twitter_consumer_key)
        val secret: String = context.getString(R.string.twitter_consumer_secret)
        presenter = DetailPresenterImpl(this, TweetRepositoryImpl(key, secret), website!!)
        presenter.loadMedia(mediaURL!!)
    }

    override fun updateTwitterUI(tweet: Tweet) {
        img_media.setImage(tweet.mediaUrl)
        if (tweet.type == "video") {
            txt_media_duration.text = tweet.duration.toString()
            txt_media_type.text = "Video"
            val regex = """(\d{2,4}x\d{2,4})""".toRegex()
            mediaList = tweet.mediaVariant
                    .filter { it.type == "video/mp4" }
                    .filter { it.bitrate != 0 }
                    .sortedBy { it.bitrate }
            val list = mediaList.map { regex.find(it.mediaUrl)?.value }
            val adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list)
            spn_media_quality.adapter = adapter

            btn_download.setOnClickListener {
                if (mediaList.isNotEmpty()) {
                    val position = spn_media_quality.selectedItemPosition
                    presenter.downloadMedia(mediaList[position].mediaUrl)
                    btn_download.isEnabled = false
                }
            }
        }
        else if (tweet.type == "photo") {
            txt_media_type.text = "Photo"
            txt_media_duration.text = "0"
            spn_media_quality.isEnabled = false
            btn_download.isEnabled = false
        }
        else {
            spn_media_quality.isEnabled = false
            txt_media_duration.text = "0"
            txt_media_type.text = "Gif"
            presenter.loadGifLinks(tweet.id)
        }
    }

    override fun updateGifUI(list: List<String>) {
        img_media.setImage(list[0])
        btn_download.setOnClickListener {
            val link = list[1]
            presenter.downloadMedia(link)
            btn_download.isEnabled = false
        }

    }

    override fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
        img_media.visibility = View.GONE
        btn_download.isEnabled = false
    }

    override fun hideProgressBar() {
        progress_bar.visibility = View.GONE
        img_media.visibility = View.VISIBLE
        btn_download.isEnabled = true
    }

    override fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
