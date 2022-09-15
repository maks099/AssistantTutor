package com.example.assistanttutor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.assistanttutor.R;
import com.example.assistanttutor.database.DBManager;
import com.example.assistanttutor.database.DBSingletone;

import java.util.ArrayList;

public class StudentArrayAdapter2 extends ArrayAdapter<String> {

    private ArrayList<String> students;
    private int courseId;

    private DBManager db;

    public StudentArrayAdapter2(@NonNull Context context, ArrayList<String> objects, int courseId) {
        super(context, 0, objects);
        this.courseId = courseId;
        this.students = objects;
        db = DBSingletone.getInstance(context).getDbManager();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if(currentItemView == null){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.student_list_item, parent, false);
        }

        TextView txtStudentName = currentItemView.findViewById(R.id.txtStudent);
        txtStudentName.setText(students.get(position));

        ImageButton btnDeleteStudent = currentItemView.findViewById(R.id.btnDeleteStudent);
        btnDeleteStudent.setOnClickListener(e -> {
            int result = db.removeStudentOnCourse(students.get(position), courseId);
            if(result != -1){
                notifyDataSetChanged();
                students.remove(position);
            }
        });

        return currentItemView;
    }
}
