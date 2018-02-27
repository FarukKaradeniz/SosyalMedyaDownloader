package com.farukkaradeniz.sosyalmedyadownloader.ui.fragments

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.farukkaradeniz.sosyalmedyadownloader.Constants
import com.farukkaradeniz.sosyalmedyadownloader.R
import com.farukkaradeniz.sosyalmedyadownloader.events.LinkEvent
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_input.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
class InputFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        btn_send_address.setOnClickListener {
            val text = edt_address.text.toString()
            val event = when {
                "twitter" in text -> Constants.TWITTER
                "instagram" in text -> Constants.INSTAGRAM
                text.isEmpty() -> ""
                else -> getString(R.string.invalid_input)
            }
            when {
                event.isEmpty() -> {
                    edt_address.error = getString(R.string.empty_text)
                }
                event == getString(R.string.invalid_input) -> {
                    edt_address.error = getString(R.string.invalid_input)
                }
                else -> {
                    EventBus.getDefault().post(LinkEvent(event, text))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboardManager.hasPrimaryClip()) {
            val clipData = clipboardManager.primaryClip
            val item = clipData.getItemAt(0)
            if (item.text.contains(Constants.TWITTER) || item.text.contains(Constants.INSTAGRAM)){
                edt_address.setText(item.text)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }
}