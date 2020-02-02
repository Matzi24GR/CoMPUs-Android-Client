package com.example.uom.announcements


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.uom.R
import kotlinx.coroutines.*


class AnnouncementsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_announcements, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.announcementsRecyclerView)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.announcement_swipeLayout)

        val adapter = AnnouncementsListAdapter(this.context!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)


        val announcementViewModel = ViewModelProvider(this).get(AnnouncementViewModel::class.java)
        announcementViewModel.allAnnouncements.observe(viewLifecycleOwner, Observer { announcements -> announcements?.let { adapter.setAnnouncements(it) } })

        swipeRefreshLayout.setOnRefreshListener {
            val job = GlobalScope.launch(Dispatchers.IO) {
                AnnouncementRepository(context!!).refreshAnnouncements()
            }
            job.invokeOnCompletion { swipeRefreshLayout.isRefreshing = false }
        }


        return view
    }
}
