package com.drugvilla.in.model.offer;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OfferBaseResponse extends BaseResponse {
    @SerializedName("offer_data")
    @Expose
    private OfferData offerData;

    public OfferData getOfferData() {
        return offerData;
    }

    public void setOfferData(OfferData offerData) {
        this.offerData = offerData;
    }
}
