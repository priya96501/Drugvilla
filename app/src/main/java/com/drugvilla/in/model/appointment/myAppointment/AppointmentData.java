package com.drugvilla.in.model.appointment.myAppointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentData {
    @SerializedName("appointment_id")
    @Expose
    private String appointment_id;
    @SerializedName("doctor_name")
    @Expose
    private String doctor_name;
    @SerializedName("doctor_fess")
    @Expose
    private String doctor_fess;
    @SerializedName("doctor_location")
    @Expose
    private String doctor_location;
    @SerializedName("doctor_profile")
    @Expose
    private String doctor_profile;
    @SerializedName("appointment_status")
    @Expose
    private String appointment_status;
    @SerializedName("appointment_date")
    @Expose
    private String appointment_date;
    @SerializedName("appointment_time")
    @Expose
    private String appointment_time;
    @SerializedName("clinic_name")
    @Expose
    private String clinic_name;
    @SerializedName("booked_for")
    @Expose
    private String booked_for;
    @SerializedName("payment_status")
    @Expose
    private String payment_status;
    @SerializedName("payment_mode")
    @Expose
    private String payment_mode;

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getBooked_for() {
        return booked_for;
    }

    public void setBooked_for(String booked_for) {
        this.booked_for = booked_for;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_fess() {
        return doctor_fess;
    }

    public void setDoctor_fess(String doctor_fess) {
        this.doctor_fess = doctor_fess;
    }

    public String getDoctor_location() {
        return doctor_location;
    }

    public void setDoctor_location(String doctor_location) {
        this.doctor_location = doctor_location;
    }

    public String getDoctor_profile() {
        return doctor_profile;
    }

    public void setDoctor_profile(String doctor_profile) {
        this.doctor_profile = doctor_profile;
    }

    public String getAppointment_status() {
        return appointment_status;
    }

    public void setAppointment_status(String appointment_status) {
        this.appointment_status = appointment_status;
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
