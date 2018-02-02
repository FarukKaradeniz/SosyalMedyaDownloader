package com.farukkaradeniz.sosyalmedyadownloader

import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by Faruk Karadeniz on 2.02.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
fun ImageView.setImage(url: String) {
    Picasso.with(context)
            .load(url)
            .into(this)
}