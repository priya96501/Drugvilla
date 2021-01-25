package com.drugvilla.in.model.order.myOrder;

import com.drugvilla.in.model.Pagination;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrderListResponse extends BaseResponse {

    @SerializedName("order_data")
    @Expose
    private List<MyOrderListData> myOrderData = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<MyOrderListData> getMyOrderData() {
        return myOrderData;
    }

    public void setMyOrderData(List<MyOrderListData> myOrderData) {
        this.myOrderData = myOrderData;
    }
}
