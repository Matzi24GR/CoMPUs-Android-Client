package com.example.uom.Fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.uom.R
import com.example.uom.Repository.AnnouncementRepository

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = context!!.getSharedPreferences("CREDENTIALS",Context.MODE_PRIVATE)

        val usernameTextView = view.findViewById<TextView>(R.id.username_textView)
        usernameTextView.text = sharedPreferences.getString("name",null)

        val unreadTextView = view.findViewById<TextView>(R.id.unread_textView)
        val unreadCount = AnnouncementRepository(context!!).unreadCount
        unreadCount.observe(this, Observer {
            unreadTextView.text = unreadCount.value.toString() }
        )
    }


}
