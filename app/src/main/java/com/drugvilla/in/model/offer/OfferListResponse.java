package com.drugvilla.in.model.offer;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OfferListResponse extends BaseResponse {
    @SerializedName("offer_data")
    @Expose
    private List<OfferListData> offerListData = null;

    public List<OfferListData> getOfferListData() {
        return offerListData;
    }

    public void setOfferListData(List<OfferListData> offerListData) {
        this.offerListData = offerListData;
    }
}
