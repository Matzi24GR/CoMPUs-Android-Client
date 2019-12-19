package com.example.uom;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView titleText;
    private final TextView profText;
    private final TextView semesterText;

    private Course course;
    private Context context;

    public CourseHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;

        this.titleText = (TextView) itemView.findViewById(R.id.title_text);
        this.profText = (TextView) itemView.findViewById(R.id.profs_text);
        this.semesterText = (TextView) itemView.findViewById(R.id.semester_text);

        itemView.setOnClickListener(this);
    }
    public  void bindCourse(Course course) {
        this.course = course;
        this.titleText.setText(course.getTitle());
        this.profText.setText(course.getProfs());
        this.semesterText.setText(Integer.toString(course.getSemester()));
    }

    @Override
    public void onClick(View v) {
        if (this.course != null) {

        }
    }


}
