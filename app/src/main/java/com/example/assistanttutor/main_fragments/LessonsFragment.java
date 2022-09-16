package com.example.assistanttutor.main_fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.assistanttutor.AddLessonActivity;
import com.example.assistanttutor.R;
import com.example.assistanttutor.adapters.LessonArrayAdapter;
import com.example.assistanttutor.database.DBManager;
import com.example.assistanttutor.database.DBSingletone;
import com.example.assistanttutor.database.objects.Course;
import com.example.assistanttutor.database.objects.Lesson;
import com.example.assistanttutor.databinding.FragmentLessonsBinding;
import com.example.assistanttutor.helpers.PdfHelper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.jetbrains.annotations.NotNull;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class LessonsFragment extends Fragment {

    private FragmentLessonsBinding binding;
    private DBManager db;
    private ArrayList<Course> courses;
    private ArrayList<String> studentsNames, themes;
    private ArrayList<Lesson> lessons, sortedLessons;

    private LessonArrayAdapter adapter;


    public LessonsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = DBSingletone.getInstance(getContext()).getDbManager();

        onCoursesSpinner();
        setSimpleSpinnerListener(binding.spnStudents);
        onAddLecture();
        onPrintReport();
    }

    private void onPrintReport() {
        binding.btnPrintReport.setOnClickListener(e -> {
            int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(sortedLessons == null || sortedLessons.size() == 0){
                Toast.makeText(getContext(), getString(R.string.nothingToSave), Toast.LENGTH_SHORT).show();
            }
            else if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1
                        );
            }
            else{
                try {
                    PdfHelper pdfHelper = new PdfHelper(getContext(), getActivity());
                    pdfHelper.createPdf(sortedLessons, binding.spnCourses.getSelectedItem().toString());
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (DocumentException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    File myFile;









    @Override
    public void onResume() {
        super.onResume();
        fetchCourses();
        fetchLessons();
    }

    private void setSimpleSpinnerListener(Spinner spn) {
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    private void showList() {
        sortedLessons = new ArrayList<>();
        int allScore = 0;
        for(Lesson lesson : lessons){
            if(
                    lesson.getCourseId() == courses.get(binding.spnCourses.getSelectedItemPosition()).getId()
                    && lesson.getName().equals(binding.spnStudents.getSelectedItem().toString())){
                sortedLessons.add(lesson);
                allScore += lesson.getScore();

            }
        }

        if(allScore!=0){
            binding.txtAvgScore.setVisibility(View.VISIBLE);
            binding.txtAvgScore.setText(getString(R.string.avgScore) + " " + (allScore / sortedLessons.size()));
        } else{
            binding.txtAvgScore.setVisibility(View.GONE);
        }

        adapter = new LessonArrayAdapter(getContext(), sortedLessons);
        binding.lstLessons.setAdapter(adapter);
    }

    private void fetchLessons() {
        lessons = new ArrayList<>();
        Cursor cursor = db.fetchLessons();

        if(cursor.moveToFirst()){
            do{
                Lesson lesson = new Lesson(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                lessons.add(lesson);
            } while (cursor.moveToNext());
        }

    }

    private void onAddLecture() {
        binding.btnAddLesson.setOnClickListener(e -> {
            if(courses.size() == 0){
                Toast.makeText(getContext(), getString(R.string.addCoursePlease), Toast.LENGTH_SHORT).show();
                return;
            } else if(studentsNames.size() == 0){
                Toast.makeText(getContext(), getString(R.string.addStudentToCourse), Toast.LENGTH_SHORT).show();
                return;
            }
            else if(themes.size() == 0){
                Toast.makeText(getContext(), getString(R.string.addThemes), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getContext(), AddLessonActivity.class);
            intent.putExtra("courseId", courses.get(binding.spnCourses.getSelectedItemPosition()).getId());
            intent.putExtra("student", binding.spnStudents.getSelectedItem().toString());
            intent.putExtra("themes", themes);
            startActivity(intent);
        });
    }

    private void onCoursesSpinner() {
        binding.spnCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int courseId = courses.get(position).getId();
                fetchStudents(courseId);
                fetchThemes(courseId);
                showList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchThemes(int courseId) {
        Cursor cursor = db.fetchThemes(courseId);
        themes = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                themes.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

    }

    private void fetchStudents(int courseId) {
        Cursor cursor = db.fetchStudentsOnCourse(courseId);
        studentsNames = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                studentsNames.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, studentsNames);
        binding.spnStudents.setAdapter(arrayAdapter);
    }



    private void fetchCourses() {
        Cursor cursor = db.fetchCourses();
        courses = new ArrayList<>();
        String[] coursesTitles = new String[cursor.getCount()];
        if(cursor.moveToFirst()) {
            do {
                Course course = new Course(cursor.getString(1), cursor.getInt(0));
                courses.add(course);
                coursesTitles[courses.size() -1] = course.getTitle();
            } while (cursor.moveToNext());
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, coursesTitles);
        binding.spnCourses.setAdapter(arrayAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLessonsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}