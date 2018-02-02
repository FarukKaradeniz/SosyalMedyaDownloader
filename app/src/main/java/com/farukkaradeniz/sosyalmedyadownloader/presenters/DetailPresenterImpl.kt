package com.farukkaradeniz.sosyalmedyadownloader.presenters

import android.util.Log
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.farukkaradeniz.sosyalmedyadownloader.Constants
import com.farukkaradeniz.sosyalmedyadownloader.events.EmptyEvent
import com.farukkaradeniz.sosyalmedyadownloader.model.BaseRepository
import com.farukkaradeniz.sosyalmedyadownloader.model.TweetRepository
import com.farukkaradeniz.sosyalmedyadownloader.ui.fragments.DetailView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
class DetailPresenterImpl(val view: DetailView, val repository: BaseRepository, val website: String): DetailPresenter {
    /**
     * Verilen link icin medya yukleme fonksiyonu cagirilir, hata olursa ekrana
     * toast mesaji gosterilir
     */
    override fun loadMedia(link: String) {
        try {
            when(website) {
                Constants.TWITTER -> loadTweet(extractTwitterId(link), repository as TweetRepository)
                else -> view.showToast("Error Loading Media!")
            }
        }catch (e: Exception) {
            view.showToast(e.localizedMessage)
        }
    }

    /**
     * Verilen id'e sahip tweet verileri API'den alinir ve arayuz guncellenir,
     * hata olmasi durumunda ekrana toast mesaji gosterilir
     */
    private fun loadTweet(id: Long, tweetRepository: TweetRepository) {
        view.showProgressBar()
        tweetRepository.getTweet(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.hideProgressBar()
                            view.updateTwitterUI(it)
                        },
                        {
                            view.showToast(it.message!!)
                            EventBus.getDefault().post(EmptyEvent())
                        })
    }

    /**
     * Verilen tweet id'si icin gif verileri bulunur ve arayuz guncellenir,
     * hata olmasi durumuna ekrana toast mesaji gosterilir
     */
    override fun loadGifLinks(id: Long) {
        view.showProgressBar()
        (repository as TweetRepository).getGifTweet(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.hideProgressBar()
                            view.updateGifUI(it)
                        },
                        {
                            view.showToast(it.message!!)
                            EventBus.getDefault().post(EmptyEvent())}
                )
    }

    /**
     * Verilen text'te ID kismi Regex kullanilarak diger kisimlardan ayirilir ve
     * bu ID cagiran methoda dondurulur
     */
    override fun extractTwitterId(link: String): Long {
        val regex = """(\d{17,20})""".toRegex()
        return regex.find(link)?.value?.toLong() ?: 0
    }

    /**
     * Verilen linkteki mp4 dosyasi indirilir.
     */
    override fun downloadMedia(link: String) {
        val filename = Date().time.toString() + ".mp4"
        PRDownloader.download(link, Constants.DOWNLOAD_DIRECTORY, filename)
                .build()
                .setOnStartOrResumeListener {
                    view.showToast("Downloading the $filename has started")
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        view.showToast("$filename is downloaded")
                    }

                    override fun onError(error: Error?) {
                        view.showToast("Error while downloading the file")
                    }
                })
    }
}
