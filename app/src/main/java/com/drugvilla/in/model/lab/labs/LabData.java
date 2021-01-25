package com.drugvilla.in.model.lab.labs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabData {
    @SerializedName("lab_id")
    @Expose
    private String lab_id;
    @SerializedName("lab_name")
    @Expose
    private String lab_name;
    @SerializedName("lab_image")
    @Expose
    private String lab_image;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("certified_by")
    @Expose
    private String certified_by;
    @SerializedName("test_done")
    @Expose
    private String test_done;
    @SerializedName("lab_rating")
    @Expose
    private String lab_rating;
    @SerializedName("test_mrp")
    @Expose
    private String test_mrp;
    @SerializedName("test_amount")
    @Expose
    private String test_amount;
    @SerializedName("test_discount")
    @Expose
    private String test_discount;

    public String getTest_mrp() {
        return test_mrp;
    }

    public void setTest_mrp(String test_mrp) {
        this.test_mrp = test_mrp;
    }

    public String getTest_amount() {
        return test_amount;
    }

    public void setTest_amount(String test_amount) {
        this.test_amount = test_amount;
    }

    public String getTest_discount() {
        return test_discount;
    }

    public void setTest_discount(String test_discount) {
        this.test_discount = test_discount;
    }

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLab_id() {
        return lab_id;
    }

    public void setLab_id(String lab_id) {
        this.lab_id = lab_id;
    }

    public String getLab_name() {
        return lab_name;
    }

    public void setLab_name(String lab_name) {
        this.lab_name = lab_name;
    }

    public String getLab_image() {
        return lab_image;
    }

    public void setLab_image(String lab_image) {
        this.lab_image = lab_image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCertified_by() {
        return certified_by;
    }

    public void setCertified_by(String certified_by) {
        this.certified_by = certified_by;
    }

    public String getTest_done() {
        return test_done;
    }

    public void setTest_done(String test_done) {
        this.test_done = test_done;
    }

    public String getLab_rating() {
        return lab_rating;
    }

    public void setLab_rating(String lab_rating) {
        this.lab_rating = lab_rating;
    }
}
