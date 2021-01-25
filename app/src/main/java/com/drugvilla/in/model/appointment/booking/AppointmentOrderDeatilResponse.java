package com.drugvilla.in.model.appointment.booking;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentOrderDeatilResponse extends BaseResponse {

    @SerializedName("appointment_order_data")
    @Expose
    private AppointmentOrderDetailData appointmentOrderDetailData;

    public AppointmentOrderDetailData getAppointmentOrderDetailData() {
        return appointmentOrderDetailData;
    }

    public void setAppointmentOrderDetailData(AppointmentOrderDetailData appointmentOrderDetailData) {
        this.appointmentOrderDetailData = appointmentOrderDetailData;
    }
}
