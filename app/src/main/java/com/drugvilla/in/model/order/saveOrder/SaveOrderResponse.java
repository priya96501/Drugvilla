package com.drugvilla.in.model.order.saveOrder;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveOrderResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private SaveOrderData saveOrderData;

    public SaveOrderData getSaveOrderData() {
        return saveOrderData;
    }

    public void setSaveOrderData(SaveOrderData saveOrderData) {
        this.saveOrderData = saveOrderData;
    }
}
