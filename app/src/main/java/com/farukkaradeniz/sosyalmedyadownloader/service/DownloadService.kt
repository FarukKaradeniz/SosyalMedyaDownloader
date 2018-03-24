package com.farukkaradeniz.sosyalmedyadownloader.service

import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.farukkaradeniz.sosyalmedyadownloader.Constants
import com.farukkaradeniz.sosyalmedyadownloader.R
import com.farukkaradeniz.sosyalmedyadownloader.events.MessageEvent
import org.greenrobot.eventbus.EventBus


/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
class DownloadService : IntentService("DownloadService") {
    private val notificationId = 1

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val fileName = it.getStringExtra(Constants.NAME)
            val link = it.getStringExtra(Constants.LINK)
            downloadMedia(link = link, fileName = fileName)
        }
    }

    /**
     * Verilen linkteki dosya indirilir.
     */
    private fun downloadMedia(link: String, fileName: String) {
        val notManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var notification = notificationBuilder(fileName)
        PRDownloader.download(link, Constants.DOWNLOAD_DIRECTORY, fileName)
                .build()
                .setOnStartOrResumeListener {
                    notManager.notify(notificationId, notification.build())
                }
                .setOnProgressListener {
                    val percentage = it.currentBytes * 100L / it.totalBytes
                    notification = notificationBuilder(fileName)
                            .setContentTitle(getString(R.string.downloading) + " (${percentage.toInt()}%)")
                            .setProgress(100, percentage.toInt(), false)
                    notManager.notify(notificationId, notification.build())
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        val intent = PendingIntent
                                .getActivity(applicationContext, 0, openMedia(fileName), PendingIntent.FLAG_ONE_SHOT)
                        notification.setOngoing(false)
                                .setContentIntent(intent)
                                .setProgress(0, 0, false)
                                .setContentTitle(getString(R.string.download_completed))
                                .setAutoCancel(true)
                        notManager.notify(notificationId, notification.build())
                        EventBus.getDefault().postSticky(MessageEvent(message = getString(R.string.download_completed)))
                    }

                    override fun onError(error: Error?) {
                        notification.setOngoing(false)
                                .setProgress(0, 0, false)
                                .setContentTitle(getString(R.string.download_failed))
                                .setAutoCancel(true)
                        notManager.notify(notificationId, notification.build())
                        EventBus.getDefault().postSticky(MessageEvent(message = getString(R.string.download_failed)))
                    }
                })
    }

    private fun notificationBuilder(content: String): Notification.Builder {
        //API 26 ve sonrasi icin deprecated. Ilerleyen zamanlarda 26 ve sonrasi icin
        //guncelleme yapilacak
        return Notification.Builder(applicationContext)
                .setContentTitle(getString(R.string.downloading))
                .setContentText(content)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_app)
                .setProgress(100, 0, false)
    }

    private fun openMedia(filename: String): Intent {
        val fileExt = filename.takeLastWhile { it.isLetterOrDigit() }
        Log.i(javaClass.simpleName, "File name: $filename  File extension: $fileExt")
        val mimeType = when (fileExt) {
            "mp4" -> "video/mp4"
            else -> "image/*"
        }
        val mediaPath = "${Constants.DOWNLOAD_DIRECTORY}/$filename"
        Log.i(javaClass.simpleName, "DIRECTORY: $mediaPath")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(mediaPath), mimeType)
        return intent
    }
}