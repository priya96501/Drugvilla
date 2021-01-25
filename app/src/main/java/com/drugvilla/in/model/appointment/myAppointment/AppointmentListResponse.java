package com.drugvilla.in.model.appointment.myAppointment;

import com.drugvilla.in.model.Pagination;
import com.drugvilla.in.model.appointment.myAppointment.AppointmentData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppointmentListResponse extends BaseResponse {
    @SerializedName("appointment_data")
    @Expose
    private List<AppointmentData> appointmentListData = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<AppointmentData> getAppointmentListData() {
        return appointmentListData;
    }

    public void setAppointmentListData(List<AppointmentData> appointmentListData) {
        this.appointmentListData = appointmentListData;
    }


}