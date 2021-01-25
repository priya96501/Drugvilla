package com.drugvilla.in.model.appointment.myAppointment;

import com.drugvilla.in.model.appointment.myAppointment.AppointmentData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentResponse extends BaseResponse {
    @SerializedName("appointment_order_data")
    @Expose
    private AppointmentData appointmentData;

    public AppointmentData getAppointmentData() {
        return appointmentData;
    }

    public void setAppointmentData(AppointmentData appointmentData) {
        this.appointmentData = appointmentData;
    }
}

