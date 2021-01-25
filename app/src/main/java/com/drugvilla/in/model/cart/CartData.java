package com.drugvilla.in.model.cart;

import com.drugvilla.in.model.cart.SelectedItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartData {
    @SerializedName("total_mrp")
    @Expose
    private String totalMrp;
    @SerializedName("total_discount")
    @Expose
    private String totalDiscount;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("selected_items")
    @Expose
    private List<SelectedItem> selectedItems = null;

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

    public List<SelectedItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<SelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
