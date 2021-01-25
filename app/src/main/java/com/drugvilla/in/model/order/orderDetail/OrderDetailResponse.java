package com.drugvilla.in.model.order.orderDetail;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailResponse extends BaseResponse {

    @SerializedName("order_data")
    @Expose
    private OrderDetailData orderDetailData;

    public OrderDetailData getOrderDetailData() {
        return orderDetailData;
    }

    public void setOrderDetailData(OrderDetailData orderDetailData) {
        this.orderDetailData = orderDetailData;
    }
}
