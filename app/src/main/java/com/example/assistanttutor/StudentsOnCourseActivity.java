package com.example.assistanttutor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.assistanttutor.adapters.StudentArrayAdapter2;
import com.example.assistanttutor.database.DBManager;
import com.example.assistanttutor.database.DBSingletone;
import com.example.assistanttutor.databinding.ActivityStudentsOnCourseBinding;

import java.util.ArrayList;

public class StudentsOnCourseActivity extends AppCompatActivity {

    private ArrayList<String> allStudents, studentsOnCourse;

    private DBManager db;
    private int courseId;

    private ArrayAdapter<String> adapter;

    private ActivityStudentsOnCourseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentsOnCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getString(R.string.studentsOnCourse));

        courseId = getIntent().getIntExtra("courseId", -1);
        db = DBSingletone.getInstance(getApplicationContext()).getDbManager();
        fetchStudentsOnCourse();


        adapter = new StudentArrayAdapter2(
                getApplicationContext(),
                studentsOnCourse,
                courseId);
        binding.lstStudentsOnCourse.setAdapter(adapter);

        fetchAllStudents();
        onAddStudentToCourse();
    }

    private void onAddStudentToCourse() {
        binding.btnAddStudent.setOnClickListener(e -> {
            if(allStudents.size() == 0){
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.noStudents),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(StudentsOnCourseActivity.this);
            dialog.setTitle(getString(R.string.enterNewStudent));
            final Spinner spinner = new Spinner(getApplicationContext());
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(StudentsOnCourseActivity.this,
                    android.R.layout.simple_spinner_item, allStudents);
            spinner.setAdapter(arrayAdapter);
            dialog.setView(spinner);

            dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newStudentName = spinner.getSelectedItem().toString().trim();
                    if(studentsOnCourse.contains(newStudentName)){
                        Toast.makeText(getApplicationContext(), getString(R.string.errorStudentNowIs), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int newStudentId = db.insertStudentToCourse(newStudentName, courseId);
                    if(newStudentId != -1){
                        adapter.add(newStudentName);
                        adapter.notifyDataSetChanged();
                    }

                }
            });

            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialog.show();
        });
    }


    private void fetchAllStudents() {
        allStudents = new ArrayList<>();
        Cursor cursor = db.fetchStudents();
        if(cursor.moveToFirst()){
            do{
                allStudents.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
    }

    private void fetchStudentsOnCourse() {
        studentsOnCourse = new ArrayList<>();
        Cursor cursor = db.fetchStudentsOnCourse(courseId);
        if(cursor.moveToFirst()){
            do{
                studentsOnCourse.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
    }
}