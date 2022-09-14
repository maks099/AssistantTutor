package com.example.assistanttutor.database.objects;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class Course implements Parcelable {

    private String title, description, planning;
    private int id;

    protected Course(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        planning = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public Course(String title, String description, String planning) {
        this.title = title;
        this.description = description;
        this.planning = planning;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlanning() {
        return planning;
    }

    public void setPlanning(String planning) {
        this.planning = planning;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(planning);
    }
}
