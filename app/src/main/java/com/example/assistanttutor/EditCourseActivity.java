package com.example.assistanttutor;

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
    DBManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityEditCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Course course = getIntent().getParcelableExtra("course");
        if(course != null){
            courseId = course.getId();
            binding.edtCourseTitle.setText(course.getTitle());
            binding.edtDescription.setText(course.getDescription());
        } else {
            binding.btnDelete.setVisibility(View.INVISIBLE);
        }
        db = DBSingletone.getInstance(getApplicationContext()).getDbManager();


        saveCourse();
        deleteCourse();
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