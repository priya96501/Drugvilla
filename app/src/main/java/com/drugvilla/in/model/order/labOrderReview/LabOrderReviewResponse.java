package com.drugvilla.in.model.order.labOrderReview;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabOrderReviewResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private LabOrderReviewData data;

    public LabOrderReviewData getData() {
        return data;
    }

    public void setData(LabOrderReviewData data) {
        this.data = data;
    }
}
