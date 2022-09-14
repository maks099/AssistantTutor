package com.example.assistanttutor.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.assistanttutor.R;

public class NewCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public NewCursorAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.simple_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleView = view.findViewById(R.id.courseTitle);
        String title = cursor.getString(0);
        titleView.setText(title);
    }
}
