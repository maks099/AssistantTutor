package com.example.assistanttutor;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.assistanttutor.database.DBManager;
import com.example.assistanttutor.database.DBSingletone;
import com.example.assistanttutor.database.objects.Lesson;
import com.example.assistanttutor.databinding.ActivityAddLessonBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class AddLessonActivity extends AppCompatActivity {

    private String student;
    private ArrayList<String> themes;
    private int courseId;
    private DBManager db;

    private ActivityAddLessonBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddLessonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = DBSingletone.getInstance(getApplicationContext()).getDbManager();
        courseId = getIntent().getIntExtra("courseId", -1);
        student = getIntent().getStringExtra("student");
        themes = getIntent().getStringArrayListExtra("themes");

        binding.txtStudent.setText(getString(R.string.student) + " " + student);


        ArrayList<String> scores = new ArrayList<>();
        for(int i = 1; i < 6; i++){
            scores.add(i+"");
        }


        setAdapterForSpinner(binding.spnScore, scores);
        setAdapterForSpinner(binding.spnTheme, themes);

        binding.npCost.setMinValue(0);
        binding.npCost.setMaxValue(10000);

        onAddLessonButton();
    }

    private void onAddLessonButton() {
        binding.btnSaveLesson.setOnClickListener(e -> {
            Lesson lesson = new Lesson(
                    courseId,
                    binding.npCost.getValue(),
                    Integer.parseInt(binding.spnScore.getSelectedItem().toString()),
                    student,
                    binding.spnTheme.getSelectedItem().toString(),
                    getCurrentDate()
            );

            if(db.insertLesson(lesson) != -1){
                onBackPressed();
            }
        });
    }

    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.DAY_OF_MONTH);
    }

    private void setAdapterForSpinner(Spinner spn, ArrayList<String> array) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, array);
        spn.setAdapter(arrayAdapter);
    }
}