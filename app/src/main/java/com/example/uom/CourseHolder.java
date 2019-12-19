package com.example.uom;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

        this.titleText =  itemView.findViewById(R.id.title_text);
        this.profText = itemView.findViewById(R.id.profs_text);
        this.semesterText = itemView.findViewById(R.id.semester_text);

        itemView.setOnClickListener(this);
    }
    public  void bindCourse(Course course) {
        this.course = course;
        this.titleText.setText(course.getTitle());
        this.profText.setText(course.getProfs());
        this.semesterText.setText(String.format(Integer.toString(course.getSemester()),"%d"));
    }

    @Override
    public void onClick(View v) {
        if (this.course != null) {
            Toast.makeText(context, course.getUrl(), Toast.LENGTH_SHORT).show();
        }
    }


}
