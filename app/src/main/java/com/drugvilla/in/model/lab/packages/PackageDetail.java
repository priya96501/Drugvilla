package com.drugvilla.in.model.lab.packages;

import com.drugvilla.in.model.ReviewData;
import com.drugvilla.in.model.lab.LabCertification;
import com.drugvilla.in.model.lab.labs.LabData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PackageDetail {
    @SerializedName("package_id")
    @Expose
    private String package_id;
    @SerializedName("mrp")
    @Expose
    private String mrp;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("package_name")
    @Expose
    private String package_name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("requirement")
    @Expose
    private String requirement;

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
    @SerializedName("sample_collection_charge")
    @Expose
    private String sample_collection_charge;

    @SerializedName("provided_by_labs")
    @Expose
    private String provided_by_labs;

    public String getProvided_by_labs() {
        return provided_by_labs;
    }

    public void setProvided_by_labs(String provided_by_labs) {
        this.provided_by_labs = provided_by_labs;
    }

    public String getSample_collection_charge() {
        return sample_collection_charge;
    }

    public void setSample_collection_charge(String sample_collection_charge) {
        this.sample_collection_charge = sample_collection_charge;
    }

    @SerializedName("test_done")
    @Expose
    private String test_done;
    @SerializedName("lab_rating")
    @Expose
    private String lab_rating;
    @SerializedName("lab_description")
    @Expose
    private String lab_description;
    @SerializedName("tests_included")
    @Expose
    private ArrayList<String> testIncludedList = null;

    @SerializedName("lab_reviews")
    @Expose
    private List<ReviewData> labReviewList = null;
    @SerializedName("lab_certification")
    @Expose
    private List<LabCertification> labCertificationList = null;

    public List<ReviewData> getLabReviewList() {
        return labReviewList;
    }

    public void setLabReviewList(List<ReviewData> labReviewList) {
        this.labReviewList = labReviewList;
    }

    public List<LabCertification> getLabCertificationList() {
        return labCertificationList;
    }

    public void setLabCertificationList(List<LabCertification> labCertificationList) {
        this.labCertificationList = labCertificationList;
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

    public String getLab_description() {
        return lab_description;
    }

    public void setLab_description(String lab_description) {
        this.lab_description = lab_description;
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

    public ArrayList<String> getTestIncludedList() {
        return testIncludedList;
    }

    public void setTestIncludedList(ArrayList<String> testIncludedList) {
        this.testIncludedList = testIncludedList;
    }
}
