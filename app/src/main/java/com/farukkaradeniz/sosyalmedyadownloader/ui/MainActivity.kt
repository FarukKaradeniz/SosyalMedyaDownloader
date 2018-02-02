package com.farukkaradeniz.sosyalmedyadownloader.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.farukkaradeniz.sosyalmedyadownloader.R
import com.farukkaradeniz.sosyalmedyadownloader.events.LinkEvent
import com.farukkaradeniz.sosyalmedyadownloader.ui.fragments.DetailFragment
import com.farukkaradeniz.sosyalmedyadownloader.ui.fragments.InputFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.lay_main_wrapper, InputFragment(), "INPUT")
                    .commit()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLinkEvent(event: LinkEvent) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.lay_main_wrapper, DetailFragment.newInstance(event.website, event.mediaURL), "DETAIL")
                .addToBackStack("ReplaceInputWithDetail")
                .commit()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

}
