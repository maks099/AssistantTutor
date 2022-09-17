package com.example.assistanttutor.main_fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.assistanttutor.EditCourseActivity;
import com.example.assistanttutor.R;
import com.example.assistanttutor.database.DBManager;
import com.example.assistanttutor.database.objects.Course;
import com.example.assistanttutor.databinding.FragmentCoursesBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class CoursesFragment extends Fragment {

    private FragmentCoursesBinding binding;
    private ListView listView;
    private DBManager dbManager;
    private SimpleCursorAdapter simpleCursorAdapter;
    private ArrayList<Course> courses;

    public CoursesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courses = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCoursesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        dbManager = new DBManager(getContext());
        dbManager.open();


        Cursor cursor = dbManager.fetchCourses();
        if(cursor.moveToFirst()){
            courses.clear();
            do{
                Course course = new Course(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
                course.setId(cursor.getInt(0));
                courses.add(course);
            } while (cursor.moveToNext());
        }

        simpleCursorAdapter = new SimpleCursorAdapter(
                getContext(),
                R.layout.course_list_item,
                cursor,
                new String[]{"title", "description"},
                new int[]{R.id.courseTitle, R.id.courseDescription},
                0
        );

        simpleCursorAdapter.notifyDataSetChanged();
        listView.setAdapter(simpleCursorAdapter);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.coursesList;
        TextView emptyView = new TextView(getContext());
        emptyView.setText(getString(R.string.emptyList));
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = courses.get(position);
                Intent intent = new Intent(getContext(), EditCourseActivity.class);
                intent.putExtra("course", course);
                startActivity(intent);
            }
        });

        binding.btnAddNewCourse.setOnClickListener(l -> {
            Intent intent = new Intent(getContext(), EditCourseActivity.class);
            startActivity(intent);
        });

    }


}