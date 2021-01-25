package com.drugvilla.in.model.reminder;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReminderBaseResponse extends BaseResponse {
    @SerializedName("reminder_data")
    @Expose
    private List<ReminderData> reminderData = null;

    public List<ReminderData> getReminderData() {
        return reminderData;
    }

    public void setReminderData(List<ReminderData> reminderData) {
        this.reminderData = reminderData;
    }
}
