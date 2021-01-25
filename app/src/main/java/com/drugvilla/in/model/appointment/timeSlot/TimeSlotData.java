package com.drugvilla.in.model.appointment.timeSlot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlotData {
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("is_selected")
    @Expose
    private String isSelected;

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
