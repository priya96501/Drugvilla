package com.drugvilla.in.model.order.orderReview.fromRX;

import com.drugvilla.in.model.PrescriptionData;
import com.drugvilla.in.model.address.AddressData;
import com.drugvilla.in.model.patient.PatientData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewData {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("pharmacist_call")
    @Expose
    private String pharmacistCall;
    @SerializedName("selected_address")
    @Expose
    private AddressData selectedAddress;
    @SerializedName("selected_patient")
    @Expose
    private PatientData selectedPatient;

    public PatientData getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(PatientData selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    @SerializedName("prescriptions")
    @Expose
    private List<PrescriptionData> prescriptionDataList = null;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPharmacistCall() {
        return pharmacistCall;
    }

    public void setPharmacistCall(String pharmacistCall) {
        this.pharmacistCall = pharmacistCall;
    }

    public AddressData getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressData selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public List<PrescriptionData> getPrescriptionDataList() {
        return prescriptionDataList;
    }

    public void setPrescriptionDataList(List<PrescriptionData> prescriptionDataList) {
        this.prescriptionDataList = prescriptionDataList;
    }
}
