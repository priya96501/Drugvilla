package com.drugvilla.in.model.order.orderReview;

import com.drugvilla.in.model.cart.SelectedItem;
import com.drugvilla.in.model.order.labOrderReview.LabOrderReviewData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderReviewResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private OrderReviewData orderReviewData;

    @SerializedName("selected_items")
    @Expose
    private List<SelectedItem> selectedItems = null;

    public OrderReviewData getOrderReviewData() {
        return orderReviewData;
    }

    public void setOrderReviewData(OrderReviewData orderReviewData) {
        this.orderReviewData = orderReviewData;
    }

    public List<SelectedItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<SelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
