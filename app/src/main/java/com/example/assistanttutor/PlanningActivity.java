package com.example.assistanttutor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.assistanttutor.database.DBManager;
import com.example.assistanttutor.database.DBSingletone;
import com.example.assistanttutor.database.objects.MyDate;
import com.example.assistanttutor.database.objects.Student;
import com.example.assistanttutor.databinding.ActivityPlanningBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class PlanningActivity extends AppCompatActivity {

    private ActivityPlanningBinding binding;
    private ArrayAdapter<String> adapter;
    private DBManager db;
    private int courseId;
    private int itemPos = -1;
    private ArrayList<MyDate> dates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlanningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getString(R.string.planning));
        db = DBSingletone.getInstance(getApplicationContext()).getDbManager();
        courseId = getIntent().getIntExtra("courseId", -1);
        adapter = new ArrayAdapter<>(this, R.layout.text_view_list_item);

        fetchData();
        listPreparing();

        onAddDate();
        onDeleteDate();
    }

    private void listPreparing() {
        binding.lstDates.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        binding.lstDates.setSelector(R.color.teal_200);

        binding.lstDates.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemPos = position;
            }
        });
    }

    private void fetchData() {
        dates = new ArrayList<>();
        Cursor cursor = db.fetchDates();
        if(cursor.moveToFirst()){
            do{
                MyDate myDate = new MyDate(
                        cursor.getInt(0),
                        courseId,
                        cursor.getString(3));
                dates.add(myDate);
                adapter.add(myDate.getDate() + " - " + cursor.getString(2));
            } while (cursor.moveToNext());
        }
        binding.lstDates.setAdapter(adapter);
    }

    private void onDeleteDate() {
        binding.btnDeleteDate.setOnClickListener(e -> {
            if(itemPos == -1){
                Toast.makeText(getApplicationContext(), getString(R.string.pickDatePlease), Toast.LENGTH_SHORT).show();
            }
            else if(db.removeDate(dates.get(itemPos).getId()) != -1){
                String item = binding.lstDates.getItemAtPosition(itemPos).toString();
                adapter.remove(item);
                dates.remove(itemPos);
                binding.lstDates.setAdapter(adapter);
                itemPos = -1;
            }

        });
    }

    private void onAddDate() {
        binding.btnAddDate.setOnClickListener(e -> {
            DatePickerDialog.OnDateSetListener setListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String newDate = year + "/" + month + "/" + dayOfMonth;
                    boolean duplicate = false;
                    for(MyDate date : dates){
                        if(date.getDate().equals(newDate)){
                            Toast.makeText(getApplicationContext(), getString(R.string.duplicateDate), Toast.LENGTH_SHORT).show();
                            duplicate = true;
                            break;
                        }
                    }

                    if(!duplicate) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(PlanningActivity.this);
                        dialog.setTitle(getString(R.string.enterThemaPlease));
                        final EditText input = new EditText(getApplicationContext());
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        dialog.setView(input);
                        dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String theme = input.getText().toString().trim();
                                if(theme.length() != 0){
                                    int newDateId = (int) db.addDateToCourse(courseId, newDate, theme);
                                    if(newDateId != -1){
                                        dates.add(new MyDate(newDateId, courseId, newDate));
                                        adapter.add(newDate + " - " + theme);
                                        binding.lstDates.setAdapter(adapter);
                                    }
                                } else Toast.makeText(getApplicationContext(), getString(R.string.enterThemaPlease), Toast.LENGTH_SHORT).show();

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
                }
            };

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(this, setListener, year, month, day);
            dialog.getDatePicker().setMinDate(new Date().getTime());
            dialog.show();

        });

    }
}