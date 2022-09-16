package com.example.assistanttutor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.assistanttutor.R;
import com.example.assistanttutor.database.objects.Lesson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LessonArrayAdapter extends ArrayAdapter<Lesson> {


    private final ArrayList<Lesson> lessons;

    public LessonArrayAdapter(@NonNull Context context, ArrayList<Lesson> lessons) {
        super(context, 0, lessons);
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if(currentItemView == null){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.lesson_list_item, parent, false);
        }

        TextView txtCost = currentItemView.findViewById(R.id.txtCostOfLesson);
        String costText = getContext().getString(R.string.cost) + " " + lessons.get(position).getCost();
        txtCost.setText(costText);

        TextView txtScore = currentItemView.findViewById(R.id.txtScore);
        String scoreText = getContext().getString(R.string.score) + " " + lessons.get(position).getScore();
        txtScore.setText(scoreText);

        TextView txtDate = currentItemView.findViewById(R.id.txtDate);
        txtDate.setText(lessons.get(position).getDate());

        TextView txtTheme = currentItemView.findViewById(R.id.txtTheme);
        txtTheme.setText(lessons.get(position).getTheme());

        return currentItemView;
    }
}
