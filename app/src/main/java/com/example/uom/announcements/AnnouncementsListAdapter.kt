package com.example.uom.announcements

import android.content.Context
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.uom.Database.Announcement
import com.example.uom.R
import java.text.SimpleDateFormat

class AnnouncementsListAdapter internal constructor(val context: Context): RecyclerView.Adapter<AnnouncementsListAdapter.AnnouncementViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var announcements = emptyList<Announcement>()

    inner class AnnouncementViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var announcementTextView: TextView = itemView.findViewById(R.id.text)
        var announcementCourseView: TextView = itemView.findViewById(R.id.course_text)
        var announcementTimeView: TextView = itemView.findViewById(R.id.time_text)
        var CardView: CardView = itemView.findViewById(R.id.CardView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val itemView = inflater.inflate(R.layout.announcement_item, parent, false)
        return AnnouncementViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val current = announcements[position]
        holder.announcementTextView.text = current.text
        holder.announcementCourseView.text = current.course
        holder.announcementTimeView.text = SimpleDateFormat("d MMM, yyyy \nhh:mm a").format(current.time)
        current.isRead = true
        AnnouncementRepository(context).setRead(current,true)
        Linkify.addLinks(holder.announcementTextView,Linkify.WEB_URLS)
    }

    internal fun setAnnouncements(announcements: List<Announcement>) {
        this.announcements = announcements
        notifyDataSetChanged()
    }

    override fun getItemCount() = announcements.size
}