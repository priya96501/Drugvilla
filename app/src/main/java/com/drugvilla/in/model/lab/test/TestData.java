package com.drugvilla.in.model.lab.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestData {

    @SerializedName("test_id")
    @Expose
    private String test_id;
    @SerializedName("mrp")
    @Expose
    private String mrp;

    @SerializedName("test_name")
    @Expose
    private String test_name;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("provided_by_labs")
    @Expose
    private String provided_by_labs;
    @SerializedName("discount")
    @Expose
    private String discount;

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
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

    public String getProvided_by_labs() {
        return provided_by_labs;
    }

    public void setProvided_by_labs(String provided_by_labs) {
        this.provided_by_labs = provided_by_labs;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
