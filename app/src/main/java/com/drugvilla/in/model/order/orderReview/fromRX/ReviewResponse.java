package com.drugvilla.in.model.order.orderReview.fromRX;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewResponse extends BaseResponse {
    @SerializedName("order_data")
    @Expose
    private ReviewData orderReviewData;

    public ReviewData getOrderReviewData() {
        return orderReviewData;
    }

    public void setOrderReviewData(ReviewData orderReviewData) {
        this.orderReviewData = orderReviewData;
    }
}
