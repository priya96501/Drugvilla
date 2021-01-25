package com.drugvilla.in.model.reminder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedDay {

    @SerializedName("day")
    @Expose
    private String day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
