package com.drugvilla.in.model.order.myOrder;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyOrderDetailResponse extends BaseResponse {

    @SerializedName("order_data")
    @Expose
    private MyOrderData myOrderData;

    public MyOrderData getMyOrderData() {
        return myOrderData;
    }

    public void setMyOrderData(MyOrderData myOrderData) {
        this.myOrderData = myOrderData;
    }
}
