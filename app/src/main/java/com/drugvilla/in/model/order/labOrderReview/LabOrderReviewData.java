package com.drugvilla.in.model.order.labOrderReview;

import com.drugvilla.in.model.address.AddressData;
import com.drugvilla.in.model.cart.SelectedItem;
import com.drugvilla.in.model.order.SelectedDateTime;
import com.drugvilla.in.model.patient.PatientData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LabOrderReviewData {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("total_mrp")
    @Expose
    private String totalMrp;
    @SerializedName("total_discount")
    @Expose
    private String totalDiscount;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("selected_address")
    @Expose
    private AddressData selectedAddress;
    @SerializedName("selected_date_time")
    @Expose
    private SelectedDateTime selectedDateTime;
    @SerializedName("selected_patient")
    @Expose
    private PatientData selectedPatient;
    @SerializedName("selected_items")
    @Expose
    private List<SelectedItem> selectedItems = null;

    public AddressData getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressData selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public PatientData getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(PatientData selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalMrp() {
        return totalMrp;
    }

    public void setTotalMrp(String totalMrp) {
        this.totalMrp = totalMrp;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public SelectedDateTime getSelectedDateTime() {
        return selectedDateTime;
    }

    public void setSelectedDateTime(SelectedDateTime selectedDateTime) {
        this.selectedDateTime = selectedDateTime;
    }

    public List<SelectedItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<SelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }


}
