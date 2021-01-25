package com.drugvilla.in.model.appointment.timeSlot;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeSlotResponse extends BaseResponse {

    @SerializedName("schedule_time")
    @Expose
    private List<TimeSlotData> timeSlotData = null;

    public List<TimeSlotData> getTimeSlotData() {
        return timeSlotData;
    }

    public void setTimeSlotData(List<TimeSlotData> timeSlotData) {
        this.timeSlotData = timeSlotData;
    }
}
