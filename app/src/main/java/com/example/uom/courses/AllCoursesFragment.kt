package com.example.uom.courses



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uom.R

class AllCoursesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_all_courses, container, false)


        val recyclerView = view.findViewById<RecyclerView>(R.id.courses_recycler_view)

        val adapter = CourseListAdapter(this.context!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val courseViewModel = ViewModelProvider(this).get(CourseViewModel::class.java)
        courseViewModel.allCourses.observe(viewLifecycleOwner, Observer { courses -> courses?.let {adapter.setCourses(it)} })

        // Inflate the layout for this fragment
        return view
    }


}
