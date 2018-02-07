package com.farukkaradeniz.sosyalmedyadownloader.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        radioGroup.clearCheck()
        radioGroup.setOnCheckedChangeListener { _, id ->
            when(id) {
                R.id.radio_twitter -> txt_enter_address.text = context.getString(R.string.enter_address_twitter)
                R.id.radio_other -> txt_enter_address.text = context.getString(R.string.enter_address_other)
                else -> txt_enter_address.text = context.getString(R.string.enter_address)
            }
        }
        radio_twitter.isChecked = true
        edt_address.setText("")
        btn_send_address.setOnClickListener {
            if (radio_twitter.isChecked) { //Eger twitter secili ise
                val twitterLink = edt_address.editableText.toString()
                if (twitterLink.isNotEmpty()) {
                    EventBus.getDefault().post(LinkEvent(Constants.TWITTER, twitterLink))
                }
                else {
                    Toast.makeText(context, "Text cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(context, "Soon...", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }
}