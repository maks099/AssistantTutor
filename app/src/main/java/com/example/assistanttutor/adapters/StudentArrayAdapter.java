package com.example.assistanttutor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.assistanttutor.R;
import com.example.assistanttutor.database.DBManager;
import com.example.assistanttutor.database.DBSingletone;
import com.example.assistanttutor.database.objects.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentArrayAdapter extends ArrayAdapter<Student> {

    private ArrayList<Student> students;
    private final DBManager db;

    public StudentArrayAdapter(@NonNull Context context, @NonNull ArrayList<Student> objects) {
        super(context, 0, objects);
        students = objects;
        db = DBSingletone.getInstance(getContext()).getDbManager();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if(currentItemView == null){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.student_list_item, parent, false);
        }

        TextView txtStudentName = currentItemView.findViewById(R.id.txtStudent);
        txtStudentName.setText(students.get(position).getName());

        ImageButton btnDeleteStudent = currentItemView.findViewById(R.id.btnDeleteStudent);
        btnDeleteStudent.setOnClickListener(e -> {
            int result = db.removeStudent(students.get(position).getId());
            if(result != -1){
                notifyDataSetChanged();
                students.remove(position);
            }
        });

        return currentItemView;
    }
}
