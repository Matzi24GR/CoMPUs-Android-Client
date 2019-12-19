package com.example.uom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CourseAdapter extends ArrayAdapter<Course> {
    CourseAdapter(Context context, ArrayList<Course> courses) {
        super(context, 0, courses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.course_item, parent, false);
        }

        TextView titleTextView = listItemView.findViewById(R.id.title_text);
        TextView profsTextView = listItemView.findViewById(R.id.profs_text);
        TextView semesterTextView = listItemView.findViewById(R.id.semester_text);

        Course currentCourse = getItem(position);

        titleTextView.setText(currentCourse.getTitle());
        profsTextView.setText(currentCourse.getProfs());
        semesterTextView.setText(Integer.toString(currentCourse.getSemester()));

        return listItemView;
    }
}
