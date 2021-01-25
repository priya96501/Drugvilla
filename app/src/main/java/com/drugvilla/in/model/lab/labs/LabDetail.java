package com.drugvilla.in.model.lab.labs;

import com.drugvilla.in.model.ReviewData;
import com.drugvilla.in.model.lab.LabCertification;
import com.drugvilla.in.model.lab.packages.PackageData;
import com.drugvilla.in.model.lab.test.TestData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LabDetail {

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
    @SerializedName("lab_description")
    @Expose
    private String lab_description;
    @SerializedName("tests_provided")
    @Expose
    private List<TestData> testProvidedList = null;
    @SerializedName("pakages_provided")
    @Expose
    private List<PackageData> packageProvidedList = null;
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

    public String getLab_description() {
        return lab_description;
    }

    public void setLab_description(String lab_description) {
        this.lab_description = lab_description;
    }

    public ArrayList<String> getTestIncludedList() {
        return testIncludedList;
    }

    public void setTestIncludedList(ArrayList<String> testIncludedList) {
        this.testIncludedList = testIncludedList;
    }

    public List<PackageData> getPackageProvidedList() {
        return packageProvidedList;
    }

    public void setPackageProvidedList(List<PackageData> packageProvidedList) {
        this.packageProvidedList = packageProvidedList;
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

    public List<TestData> getTestProvidedList() {
        return testProvidedList;
    }

    public void setTestProvidedList(List<TestData> testProvidedList) {
        this.testProvidedList = testProvidedList;
    }
}
