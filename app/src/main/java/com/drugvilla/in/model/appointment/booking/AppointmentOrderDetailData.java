package com.drugvilla.in.model.appointment.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentOrderDetailData {
    @SerializedName("appointment_order_id")
    @Expose
    private String appointment_order_id;

    @SerializedName("booked_for")
    @Expose
    private String booked_for;

    @SerializedName("order_total")
    @Expose
    private String order_total;
    @SerializedName("order_status")
    @Expose
    private String order_status;
    @SerializedName("appointment_date")
    @Expose
    private String appointment_date;
    @SerializedName("appointment_time")
    @Expose
    private String appointment_time;

    public String getAppointment_order_id() {
        return appointment_order_id;
    }

    public void setAppointment_order_id(String appointment_order_id) {
        this.appointment_order_id = appointment_order_id;
    }

    public String getBooked_for() {
        return booked_for;
    }

    public void setBooked_for(String booked_for) {
        this.booked_for = booked_for;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }
}
