package com.farukkaradeniz.sosyalmedyadownloader.ui.fragments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.farukkaradeniz.sosyalmedyadownloader.*
import com.farukkaradeniz.sosyalmedyadownloader.events.MessageEvent
import com.farukkaradeniz.sosyalmedyadownloader.model.InstagramRepositoryImpl
import com.farukkaradeniz.sosyalmedyadownloader.model.TweetRepositoryImpl
import com.farukkaradeniz.sosyalmedyadownloader.model.data.InstagramPost
import com.farukkaradeniz.sosyalmedyadownloader.model.data.Tweet
import com.farukkaradeniz.sosyalmedyadownloader.model.data.TweetMediaVariant
import com.farukkaradeniz.sosyalmedyadownloader.presenters.DetailPresenter
import com.farukkaradeniz.sosyalmedyadownloader.presenters.DetailPresenterImpl
import com.farukkaradeniz.sosyalmedyadownloader.service.DownloadService
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

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
    private val requestId = 100

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
        presenter.unsubscribe()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val key: String = context.getString(R.string.twitter_consumer_key)
        val secret: String = context.getString(R.string.twitter_consumer_secret)
        if (website!! == Constants.TWITTER) {
            presenter = DetailPresenterImpl(this, TweetRepositoryImpl(key, secret), website!!)
        } else if (website!! == Constants.INSTAGRAM) {
            presenter = DetailPresenterImpl(this, InstagramRepositoryImpl(), website!!)
        }
        presenter.loadMedia(mediaURL!!)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun updateTwitterUI(tweet: Tweet, list: List<String?>) {
        img_media.setImage(tweet.mediaUrl)
        when {
            tweet.type == "video" -> {
                txt_media_duration.text = tweet.videoDuration
                txt_media_type.text = "Video"
                mediaList = tweet.mediaVariant
                val adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list)
                spn_media_quality.adapter = adapter

                btn_download.setOnClickListener {
                    if (mediaList.isNotEmpty()) {
                        val position = spn_media_quality.selectedItemPosition
                        download(link = mediaList[position].mediaUrl, mediaExtension = "mp4")
                    }
                }
            }
            tweet.type == "photo" -> {
                txt_media_type.text = "Photo"
                txt_media_duration.text = "0"
                spn_media_quality.isEnabled = false
                btn_download.isEnabled = false
            }
            else -> {
                spn_media_quality.isEnabled = false
                txt_media_duration.text = "0"
                txt_media_type.text = "Gif"
                presenter.loadGifLinks(tweet.id)
            }
        }
    }

    override fun updateGifUI(list: List<String>) {
        img_media.setImage(list[0])
        btn_download.setOnClickListener {
            val link = list[1]
            download(link = link, mediaExtension = "mp4")
        }
    }

    override fun updateInstagramUI(post: InstagramPost) {
        img_media.setImage(post.previewImageUrl)
        spn_media_quality.isEnabled = false
        txt_media_duration.text = post.duration
        if (post.type == "video") {
            txt_media_type.text = "Video"
            btn_download.setOnClickListener {
                download(link = post.videoUrl ?: "", mediaExtension = "mp4")
            }
        } else { //image
            txt_media_type.text = "Photo"
            btn_download.setOnClickListener {
                download(link = post.previewImageUrl, mediaExtension = post.previewImageUrl.takeLastWhile { it != '.' })
            }
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

    private fun download(name: String = Date().time.toString(), link: String, mediaExtension: String) {
        //Eger kullanici diske yazmak icin izin vermediyse izin istenilir.
        if (!context.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            context.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, requestId)
        } else { //kullanici diske yazma iznini verdiyse indirme islemi baslatilir
            val downloadService = Intent(activity, DownloadService::class.java)
            downloadService.apply {
                putExtra(Constants.NAME, name + "." + mediaExtension)
                putExtra(Constants.LINK, link)
            }
            activity.startService(downloadService)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(message: MessageEvent) {
        showToast(message.message)
    }
}