package com.example.uom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
    private final ArrayList<Course> courses;
    private Context context;
    private int itemResource;

    public CourseAdapter(Context context, int itemResource, ArrayList<Course> courses) {
        this.courses = courses;
        this.context = context;
        this.itemResource = itemResource;
    }

    @Override
    public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);
        return new CourseHolder(this.context, view);
    }

    @Override
    public void onBindViewHolder(CourseHolder holder, int position) {

        // 5. Use position to access the correct Bakery object
        Course course = this.courses.get(position);

        // 6. Bind the bakery object to the holder
        holder.bindCourse(course);
    }

    @Override
    public int getItemCount() {

        return this.courses.size();
    }

}
