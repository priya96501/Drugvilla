package com.drugvilla.in.model.appointment.booking;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfirmBookingResponse extends BaseResponse {
    @SerializedName("appointment_order_data")
    @Expose
    private ConfirmBookingData confirmBookingData;

    public ConfirmBookingData getConfirmBookingData() {
        return confirmBookingData;
    }

    public void setConfirmBookingData(ConfirmBookingData confirmBookingData) {
        this.confirmBookingData = confirmBookingData;
    }
}