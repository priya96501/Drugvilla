package com.drugvilla.in.model.order.orderReview;

import com.drugvilla.in.model.PrescriptionData;
import com.drugvilla.in.model.address.AddressData;
import com.drugvilla.in.model.cart.SelectedItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderReviewData {
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
    @SerializedName("is_prescribable")
    @Expose
    private String isPrescribable;
    @SerializedName("selected_address")
    @Expose
    private AddressData selectedAddress;
    @SerializedName("prescriptions")
    @Expose
    private List<PrescriptionData> prescriptionDataList = null;

   /* @SerializedName("selected_items")
    @Expose
    private List<SelectedItem> selectedItems = null;*/

    public List<PrescriptionData> getPrescriptionDataList() {
        return prescriptionDataList;
    }

    public void setPrescriptionDataList(List<PrescriptionData> prescriptionDataList) {
        this.prescriptionDataList = prescriptionDataList;
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

    public String getIsPrescribable() {
        return isPrescribable;
    }

    public void setIsPrescribable(String isPrescribable) {
        this.isPrescribable = isPrescribable;
    }

    public AddressData getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressData selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

   /* public List<SelectedItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<SelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }*/
}
