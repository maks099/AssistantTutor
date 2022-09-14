package com.example.assistanttutor;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.assistanttutor.main_fragments.CoursesFragment;
import com.example.assistanttutor.main_fragments.LessonsFragment;
import com.example.assistanttutor.main_fragments.StudentsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    private BottomNavigationView bottom_nav;

    private CoursesFragment coursesFragment;
    private LessonsFragment lessonsFragment;
    private StudentsFragment studentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coursesFragment = new CoursesFragment();
        lessonsFragment = new LessonsFragment();
        studentsFragment = new StudentsFragment();

        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.courses);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.courses: {
                changeFragment(coursesFragment);
                return true;
            }
            case R.id.lessons: {
                changeFragment(lessonsFragment);
                return true;
            }
            case R.id.students: {
                changeFragment(studentsFragment);
                return true;
            }

        }
        return false;
    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}