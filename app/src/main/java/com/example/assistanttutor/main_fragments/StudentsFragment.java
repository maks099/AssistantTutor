package com.example.assistanttutor.main_fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.assistanttutor.R;
import com.example.assistanttutor.adapters.StudentArrayAdapter;
import com.example.assistanttutor.database.DBManager;
import com.example.assistanttutor.database.DBSingletone;
import com.example.assistanttutor.database.objects.MyDate;
import com.example.assistanttutor.database.objects.Student;
import com.example.assistanttutor.databinding.FragmentCoursesBinding;
import com.example.assistanttutor.databinding.FragmentStudentsBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StudentsFragment extends Fragment {

    private FragmentStudentsBinding binding;
    private ArrayList<Student> students;
    private DBManager db;

    private StudentArrayAdapter adapter;


    public StudentsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DBSingletone.getInstance(getContext()).getDbManager();
        students = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
        fillList();
        binding.btnAddNewStudent.setOnClickListener(a -> onAddNewStudent());
    }

    private void onAddNewStudent() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(getString(R.string.enterNewStudent));
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        dialog.setView(input);

        dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newStudentName = input.getText().toString().trim();
                if(newStudentName.length() != 0){
                    int newStudentId = db.insertStudent(newStudentName);
                    if(newStudentId != -1){
                        students.add(new Student(newStudentId, newStudentName));
                        fillList();
                    }
                } else Toast.makeText(getContext(), getString(R.string.enterStudentNamePlease), Toast.LENGTH_SHORT).show();

            }
        });

        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void fillList() {
        adapter = new StudentArrayAdapter(getContext(), students);
        binding.lstStudents.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void fetchData() {
        Cursor cursor = db.fetchStudents();
        if(cursor.moveToFirst()){
            do{
                Student student = new Student(
                        cursor.getInt(0),
                        cursor.getString(1));
                students.add(student);
            } while (cursor.moveToNext());
        }
    }
}