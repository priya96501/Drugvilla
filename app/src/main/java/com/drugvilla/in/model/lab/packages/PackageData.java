package com.drugvilla.in.model.lab.packages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PackageData {

    @SerializedName("package_id")
    @Expose
    private String package_id;

    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("mrp")
    @Expose
    private String mrp;
    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("package_name")
    @Expose
    private String package_name;
    @SerializedName("provided_by_labs")
    @Expose
    private String provided_by_labs;

    @SerializedName("certified_by")
    @Expose
    private String certified_by;

    @SerializedName("tests_included")
    @Expose
    private ArrayList<String> testIncludedList = null;

    public String getCertified_by() {
        return certified_by;
    }

    public void setCertified_by(String certified_by) {
        this.certified_by = certified_by;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
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

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getProvided_by_labs() {
        return provided_by_labs;
    }

    public void setProvided_by_labs(String provided_by_labs) {
        this.provided_by_labs = provided_by_labs;
    }

    public ArrayList<String> getTestIncludedList() {
        return testIncludedList;
    }

    public void setTestIncludedList(ArrayList<String> testIncludedList) {
        this.testIncludedList = testIncludedList;
    }
}
