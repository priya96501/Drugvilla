package com.drugvilla.in.model.order.myOrder;

import com.drugvilla.in.model.address.AddressData;
import com.drugvilla.in.model.cart.SelectedItem;
import com.drugvilla.in.model.orderCancellation.CancellationData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrderData {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("user_name")
    @Expose
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @SerializedName("user_order_id")
    @Expose
    private String userOrderId;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("is_prescription_verified")
    @Expose
    private Integer isPrescriptionVerified;
    @SerializedName("prescription_verification_call_duration")
    @Expose
    private String prescriptionVerificationCallDuration;
    @SerializedName("order_type")
    @Expose
    private String orderType;
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
    @SerializedName("selected_address")
    @Expose
    private AddressData selectedAddress;
    @SerializedName("cancellation")
    @Expose
    private CancellationData cancellationData;
    @SerializedName("selected_items")
    @Expose
    private List<SelectedItem> selectedItems = null;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(String userOrderId) {
        this.userOrderId = userOrderId;
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

    public Integer getIsPrescriptionVerified() {
        return isPrescriptionVerified;
    }

    public void setIsPrescriptionVerified(Integer isPrescriptionVerified) {
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

    public AddressData getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressData selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public CancellationData getCancellationData() {
        return cancellationData;
    }

    public void setCancellationData(CancellationData cancellationData) {
        this.cancellationData = cancellationData;
    }

    public List<SelectedItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<SelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
