package com.farukkaradeniz.sosyalmedyadownloader

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Faruk Karadeniz on 2.02.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
fun ImageView.setImage(url: String) =
        Picasso.with(context)
                .load(url)
                .into(this)

fun Context.isPermissionGranted(permission: String) =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.requestPermission(permission: String, requestId: Int) =
        ActivityCompat.requestPermissions(this as AppCompatActivity, arrayOf(permission), requestId)

fun Disposable.addTo(disposable: CompositeDisposable) =
        disposable.add(this)