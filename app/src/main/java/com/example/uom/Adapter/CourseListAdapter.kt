package com.example.uom.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.uom.Database.Course
import com.example.uom.R

class CourseListAdapter internal constructor(val context: Context): RecyclerView.Adapter<CourseListAdapter.CourseViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var courses = emptyList<Course>()

    inner class CourseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var courseTitleView: TextView = itemView.findViewById(R.id.title_text)
        var courseProfsView: TextView = itemView.findViewById(R.id.profs_text)
        var courseSemesterView: TextView = itemView.findViewById(R.id.semester_text)
        var CardView: CardView = itemView.findViewById(R.id.CardView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemView = inflater.inflate(R.layout.course_item, parent, false)
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val current = courses[position]
        holder.courseTitleView.text = current.Title
        holder.courseProfsView.text = current.Profs
        holder.courseSemesterView.text = current.Semester.toString()

        holder.CardView.setOnClickListener{
            //Toast.makeText(context, current.Title, Toast.LENGTH_SHORT).show()
            val bundle = bundleOf(
                    "parent" to current.Url,
                    "URL" to current.Url
            )
            it.findNavController().navigate(R.id.action_toDirectoryFragment,bundle)
        }
    }

    internal fun setCourses(courses: List<Course>) {
        this.courses = courses
        notifyDataSetChanged()
    }

    override fun getItemCount() = courses.size
}