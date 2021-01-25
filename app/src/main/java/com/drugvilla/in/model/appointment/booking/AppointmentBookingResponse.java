package com.drugvilla.in.model.appointment.booking;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentBookingResponse extends BaseResponse {
    @SerializedName("appointment_data")
    @Expose
    private ConfirmBookingData appointmentBookingData;

    public ConfirmBookingData getAppointmentBookingData() {
        return appointmentBookingData;
    }

    public void setAppointmentBookingData(ConfirmBookingData appointmentBookingData) {
        this.appointmentBookingData = appointmentBookingData;
    }
}
