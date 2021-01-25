package com.drugvilla.in.model.reminder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReminderData {

    @SerializedName("reminder_id")
    @Expose
    private String reminderId;
    @SerializedName("medicine_name")
    @Expose
    private String medicineName;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("reminder_duration")
    @Expose
    private String reminderDuration;
    @SerializedName("selected_days")
    @Expose
    private String selectedDays;
    @SerializedName("morning")
    @Expose
    private String morning;
    @SerializedName("afternoon")
    @Expose
    private String afternoon;
    @SerializedName("evening")
    @Expose
    private String evening;

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getReminderDuration() {
        return reminderDuration;
    }

    public void setReminderDuration(String reminderDuration) {
        this.reminderDuration = reminderDuration;
    }

    public String getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(String selectedDays) {
        this.selectedDays = selectedDays;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public String getEvening() {
        return evening;
    }

    public void setEvening(String evening) {
        this.evening = evening;
    }

}
