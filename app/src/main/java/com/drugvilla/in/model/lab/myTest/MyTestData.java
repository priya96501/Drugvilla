package com.drugvilla.in.model.lab.myTest;

import com.drugvilla.in.model.PrescriptionData;
import com.drugvilla.in.model.address.AddressData;
import com.drugvilla.in.model.cart.SelectedItem;
import com.drugvilla.in.model.order.SelectedDateTime;
import com.drugvilla.in.model.orderCancellation.CancellationData;
import com.drugvilla.in.model.patient.PatientData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyTestData {

    @SerializedName("labtest_id")
    @Expose
    private String labtestId;
    @SerializedName("user_labtest_id")
    @Expose
    private String userLabtestId;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("is_prescription_verified")
    @Expose
    private String isPrescriptionVerified;
    @SerializedName("prescription_verification_call_duration")
    @Expose
    private String prescriptionVerificationCallDuration;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("test_collection_type")
    @Expose
    private String testCollectionType;
    @SerializedName("order_amount")
    @Expose
    private String orderAmount;
    @SerializedName("extra_charges")
    @Expose
    private String extraCharges;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("cancellation")
    @Expose
    private CancellationData cancellation;
    @SerializedName("selected_address")
    @Expose
    private AddressData selectedAddress;
    @SerializedName("selected_date_time")
    @Expose
    private SelectedDateTime selectedDateTime;
    @SerializedName("selected_patient")
    @Expose
    private PatientData selectedPatient;
    @SerializedName("prescriptions")
    @Expose
    private List<PrescriptionData> prescriptions = null;
    @SerializedName("order_items")
    @Expose
    private List<SelectedItem> orderItems = null;

    public String getLabtestId() {
        return labtestId;
    }

    public void setLabtestId(String labtestId) {
        this.labtestId = labtestId;
    }

    public String getUserLabtestId() {
        return userLabtestId;
    }

    public void setUserLabtestId(String userLabtestId) {
        this.userLabtestId = userLabtestId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getIsPrescriptionVerified() {
        return isPrescriptionVerified;
    }

    public void setIsPrescriptionVerified(String isPrescriptionVerified) {
        this.isPrescriptionVerified = isPrescriptionVerified;
    }

    public String getPrescriptionVerificationCallDuration() {
        return prescriptionVerificationCallDuration;
    }

    public void setPrescriptionVerificationCallDuration(String prescriptionVerificationCallDuration) {
        this.prescriptionVerificationCallDuration = prescriptionVerificationCallDuration;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTestCollectionType() {
        return testCollectionType;
    }

    public void setTestCollectionType(String testCollectionType) {
        this.testCollectionType = testCollectionType;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(String extraCharges) {
        this.extraCharges = extraCharges;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public CancellationData getCancellation() {
        return cancellation;
    }

    public void setCancellation(CancellationData cancellation) {
        this.cancellation = cancellation;
    }

    public AddressData getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressData selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public SelectedDateTime getSelectedDateTime() {
        return selectedDateTime;
    }

    public void setSelectedDateTime(SelectedDateTime selectedDateTime) {
        this.selectedDateTime = selectedDateTime;
    }

    public PatientData getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(PatientData selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public List<PrescriptionData> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<PrescriptionData> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<SelectedItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<SelectedItem> orderItems) {
        this.orderItems = orderItems;
    }
}
