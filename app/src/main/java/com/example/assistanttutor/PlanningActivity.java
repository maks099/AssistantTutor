package com.example.assistanttutor;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.assistanttutor.database.DBManager;
import com.example.assistanttutor.database.DBSingletone;
import com.example.assistanttutor.database.objects.MyDate;
import com.example.assistanttutor.databinding.ActivityPlanningBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class PlanningActivity extends AppCompatActivity {

    private ActivityPlanningBinding binding;
    private ArrayAdapter<String> adapter;
    private DBManager db;
    private String courseTitle;
    private int itemPos = -1;
    private ArrayList<MyDate> dates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlanningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = DBSingletone.getInstance(getApplicationContext()).getDbManager();
        courseTitle = getIntent().getStringExtra("courseTitle");
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
                        courseTitle,
                        cursor.getString(2));
                dates.add(myDate);
                adapter.add(myDate.getDate());
            } while (cursor.moveToNext());
        }
        System.out.println(dates.size() + " /////////////////");
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
                        int newDateId = (int) db.addDateToCourse(courseTitle, newDate);
                        if(newDateId != -1){
                            dates.add(new MyDate(newDateId, courseTitle, newDate));
                            adapter.add(newDate);
                            binding.lstDates.setAdapter(adapter);
                        }
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