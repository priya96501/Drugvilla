package com.drugvilla.in.model.patient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientData2 {

    @SerializedName("Patient_id")
    @Expose
    private String patientId;
    @SerializedName("Patient_name")
    @Expose
    private String name;

    @SerializedName("Patient_email")
    @Expose
    private String email;
    @SerializedName("Patient_age")
    @Expose
    private String age;
    @SerializedName("Patient_gender")
    @Expose
    private String gender;
    private boolean selected;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
