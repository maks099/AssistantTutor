package com.example.assistanttutor;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.assistanttutor.database.DBManager;
import com.example.assistanttutor.database.DBSingletone;
import com.example.assistanttutor.database.objects.Course;
import com.example.assistanttutor.databinding.ActivityEditCourseBinding;

public class EditCourseActivity extends AppCompatActivity {

    private ActivityEditCourseBinding binding;
    private int courseId = -1;
    private Course course;
    DBManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityEditCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        course = getIntent().getParcelableExtra("course");
        if(course != null){
            courseId = course.getId();
            binding.edtCourseTitle.setText(course.getTitle());
            binding.edtDescription.setText(course.getDescription());
            setTitle(getString(R.string.editCourse));
        } else {
            binding.btnDelete.setVisibility(View.INVISIBLE);
            setTitle(getString(R.string.createCourse));

        }
        db = DBSingletone.getInstance(getApplicationContext()).getDbManager();


        saveCourse();
        deleteCourse();
        onCalendarPlanning();
        onStudentsOnCourse();
    }

    private void onStudentsOnCourse() {
        binding.btnStudentsOnCourse.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), StudentsOnCourseActivity.class);
            intent.putExtra("courseId", course.getId());
            startActivity(intent);
        });
    }

    private void onCalendarPlanning() {
        binding.btnCalendarPlanning.setOnClickListener(e -> {
            if(course == null){
                Toast.makeText(getApplicationContext(), getString(R.string.saveCoursePlease), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, PlanningActivity.class);
            intent.putExtra("courseId", course.getId());
            startActivity(intent);
        });
    }

    private void deleteCourse() {
        binding.btnDelete.setOnClickListener(e -> {
            long result = db.removeCourse(courseId);
            checkResult(result);
        });
    }

    private void saveCourse() {
        binding.btnSave.setOnClickListener(e -> {
            String title = binding.edtCourseTitle.getText().toString().trim();
            String desc = binding.edtDescription.getText().toString().trim();
            if(title.length() == 0){
                Toast.makeText(getApplicationContext(), getString(R.string.enterTitlePlease), Toast.LENGTH_SHORT).show();
                return;
            }

            Course course = new Course(title, desc, "123");

            long result  = courseId == -1
                    ? db.insertCourse(course)
                    : db.updateCourse(course, courseId);
            checkResult(result);
        });
    }

    private void checkResult(long result) {
        if(result == -1){
            Toast.makeText(getApplicationContext(), getString(R.string.errorCourseAdding), Toast.LENGTH_SHORT).show();
        } else {
            onBackPressed();
        }
    }


}