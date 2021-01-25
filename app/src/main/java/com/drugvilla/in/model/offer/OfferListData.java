package com.drugvilla.in.model.offer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferListData {
    @SerializedName("offer_id")
    @Expose
    private String offerId;
    @SerializedName("offer_name")
    @Expose
    private String offerName;
    @SerializedName("offer_description")
    @Expose
    private String offerDescription;
    @SerializedName("offer_banner")
    @Expose
    private String offerBanner;
    @SerializedName("offer_expiry_date")
    @Expose
    private String offerExpiryDate;
    @SerializedName("offer_type")
    @Expose
    private String offerType;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getOfferBanner() {
        return offerBanner;
    }

    public void setOfferBanner(String offerBanner) {
        this.offerBanner = offerBanner;
    }

    public String getOfferExpiryDate() {
        return offerExpiryDate;
    }

    public void setOfferExpiryDate(String offerExpiryDate) {
        this.offerExpiryDate = offerExpiryDate;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

}

