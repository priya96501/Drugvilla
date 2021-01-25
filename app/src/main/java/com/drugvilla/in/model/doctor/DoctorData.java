package com.drugvilla.in.model.doctor;

import com.drugvilla.in.model.ReviewData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorData {
    @SerializedName("doctor_category_id")
    @Expose
    private String doctorCategoryId;
    @SerializedName("doctor_category_name")
    @Expose
    private String doctorCategoryName;
    @SerializedName("doctor_name")
    @Expose
    private String doctorName;
    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("doctor_profile")
    @Expose
    private String doctorProfile;
    @SerializedName("doctor_location")
    @Expose
    private String doctorLocation;
    @SerializedName("doctor_fees")
    @Expose
    private String doctorFees;
    @SerializedName("doctor_experience")
    @Expose
    private String doctorExperience;
    @SerializedName("doctor_qualification")
    @Expose
    private String doctorQualification;
    @SerializedName("doctor_rating")
    @Expose
    private String doctorRating;
    @SerializedName("doctor_specializations")
    @Expose
    private String doctorSpecializations;
    @SerializedName("doctor_registration")
    @Expose
    private String doctorRegistration;
    @SerializedName("doctor_timings")
    @Expose
    private String doctorTimings;
    @SerializedName("doctor_award")
    @Expose
    private String doctorAward;
    @SerializedName("doctor_feedback")
    @Expose
    private List<ReviewData> doctorFeedbackList = null;

    public List<ReviewData> getDoctorFeedbackList() {
        return doctorFeedbackList;
    }

    public void setDoctorFeedbackList(List<ReviewData> doctorFeedbackList) {
        this.doctorFeedbackList = doctorFeedbackList;
    }

    public String getDoctorCategoryId() {
        return doctorCategoryId;
    }

    public void setDoctorCategoryId(String doctorCategoryId) {
        this.doctorCategoryId = doctorCategoryId;
    }

    public String getDoctorCategoryName() {
        return doctorCategoryName;
    }

    public void setDoctorCategoryName(String doctorCategoryName) {
        this.doctorCategoryName = doctorCategoryName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorProfile() {
        return doctorProfile;
    }

    public void setDoctorProfile(String doctorProfile) {
        this.doctorProfile = doctorProfile;
    }

    public String getDoctorLocation() {
        return doctorLocation;
    }

    public void setDoctorLocation(String doctorLocation) {
        this.doctorLocation = doctorLocation;
    }

    public String getDoctorFees() {
        return doctorFees;
    }

    public void setDoctorFees(String doctorFees) {
        this.doctorFees = doctorFees;
    }

    public String getDoctorExperience() {
        return doctorExperience;
    }

    public void setDoctorExperience(String doctorExperience) {
        this.doctorExperience = doctorExperience;
    }

    public String getDoctorQualification() {
        return doctorQualification;
    }

    public void setDoctorQualification(String doctorQualification) {
        this.doctorQualification = doctorQualification;
    }

    public String getDoctorRating() {
        return doctorRating;
    }

    public void setDoctorRating(String doctorRating) {
        this.doctorRating = doctorRating;
    }

    public String getDoctorSpecializations() {
        return doctorSpecializations;
    }

    public void setDoctorSpecializations(String doctorSpecializations) {
        this.doctorSpecializations = doctorSpecializations;
    }

    public String getDoctorRegistration() {
        return doctorRegistration;
    }

    public void setDoctorRegistration(String doctorRegistration) {
        this.doctorRegistration = doctorRegistration;
    }

    public String getDoctorTimings() {
        return doctorTimings;
    }

    public void setDoctorTimings(String doctorTimings) {
        this.doctorTimings = doctorTimings;
    }

    public String getDoctorAward() {
        return doctorAward;
    }

    public void setDoctorAward(String doctorAward) {
        this.doctorAward = doctorAward;
    }


}
