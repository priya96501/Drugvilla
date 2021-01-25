package com.drugvilla.in.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedDateTime {

    @SerializedName("selected_date")
    @Expose
    private String selectedDate;
    @SerializedName("selected_time")
    @Expose
    private String selectedTime;

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }
}
