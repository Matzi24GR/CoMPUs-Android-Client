package com.example.uom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uom.Database.Announcement

class AnnouncementsListAdapter internal constructor(context: Context): RecyclerView.Adapter<AnnouncementsListAdapter.AnnouncementViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var announcements = emptyList<Announcement>()

    inner class AnnouncementViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var announcementItemView: TextView = itemView.findViewById(R.id.text)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val itemView = inflater.inflate(R.layout.announcement_item, parent, false)
        return AnnouncementViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val current = announcements[position]
        holder.announcementItemView.text = current.announcement
    }

    internal fun setAnnouncements(announcements: List<Announcement>) {
        this.announcements = announcements
        notifyDataSetChanged()
    }

    override fun getItemCount() = announcements.size
}