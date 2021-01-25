package com.drugvilla.in.model.appointment.booking.review;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentReviewResponse extends BaseResponse {
    @SerializedName("appointment_order_data")
    @Expose
    private AppointmentReviewData appointmentReviewData;

    public AppointmentReviewData getAppointmentReviewData() {
        return appointmentReviewData;
    }

    public void setAppointmentReviewData(AppointmentReviewData appointmentReviewData) {
        this.appointmentReviewData = appointmentReviewData;
    }
}
