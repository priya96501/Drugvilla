package com.drugvilla.in.model.dashboard.labDashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LabDashboardData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("mrp")
    @Expose
    private String mrp;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("provided_by_labs_count")
    @Expose
    private String providedByLabsCount;
    @SerializedName("certified_by")
    @Expose
    private String certifiedBy;
    @SerializedName("provided_by_lab")
    @Expose
    private String providedByLab;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("tests_included")
    @Expose
    private List<String> testsIncluded = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvidedByLabsCount() {
        return providedByLabsCount;
    }

    public void setProvidedByLabsCount(String providedByLabsCount) {
        this.providedByLabsCount = providedByLabsCount;
    }

    public String getCertifiedBy() {
        return certifiedBy;
    }

    public void setCertifiedBy(String certifiedBy) {
        this.certifiedBy = certifiedBy;
    }

    public String getProvidedByLab() {
        return providedByLab;
    }

    public void setProvidedByLab(String providedByLab) {
        this.providedByLab = providedByLab;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public List<String> getTestsIncluded() {
        return testsIncluded;
    }

    public void setTestsIncluded(List<String> testsIncluded) {
        this.testsIncluded = testsIncluded;
    }

}
