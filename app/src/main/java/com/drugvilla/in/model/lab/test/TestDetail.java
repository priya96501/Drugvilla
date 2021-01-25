package com.drugvilla.in.model.lab.test;

import com.drugvilla.in.model.lab.labs.LabData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TestDetail {
    @SerializedName("test_id")
    @Expose
    private String test_id;

    @SerializedName("test_name")
    @Expose
    private String test_name;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("mrp")
    @Expose
    private String mrp;

    @SerializedName("certified_by")
    @Expose
    private String certified_by;

    public String getCertified_by() {
        return certified_by;
    }

    public void setCertified_by(String certified_by) {
        this.certified_by = certified_by;
    }

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("requirement")
    @Expose
    private String requirement;

    @SerializedName("tests_included")
    @Expose
    private ArrayList<String> testIncludedList = null;
    @SerializedName("selected_lab_id")
    @Expose
    private String selected_lab_id;
    @SerializedName("selected_lab_name")
    @Expose
    private String selected_lab_name;
    @SerializedName("selected_lab_certification")
    @Expose
    private String selected_lab_certification;
    @SerializedName("provided_by_lab")
    @Expose
    private List<LabData> providedByLabsList = null;

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public ArrayList<String> getTestIncludedList() {
        return testIncludedList;
    }

    public void setTestIncludedList(ArrayList<String> testIncludedList) {
        this.testIncludedList = testIncludedList;
    }

    public String getSelected_lab_id() {
        return selected_lab_id;
    }

    public void setSelected_lab_id(String selected_lab_id) {
        this.selected_lab_id = selected_lab_id;
    }

    public String getSelected_lab_name() {
        return selected_lab_name;
    }

    public void setSelected_lab_name(String selected_lab_name) {
        this.selected_lab_name = selected_lab_name;
    }

    public String getSelected_lab_certification() {
        return selected_lab_certification;
    }

    public void setSelected_lab_certification(String selected_lab_certification) {
        this.selected_lab_certification = selected_lab_certification;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public List<LabData> getProvidedByLabsList() {
        return providedByLabsList;
    }

    public void setProvidedByLabsList(List<LabData> providedByLabsList) {
        this.providedByLabsList = providedByLabsList;
    }
}
