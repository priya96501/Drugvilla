package com.drugvilla.in.model.offer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferData {
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
    @SerializedName("offer_code")
    @Expose
    private String offerCode;
    @SerializedName("offer_terms_conditions")
    @Expose
    private String offerTermsConditions;
    @SerializedName("offer_eligibility")
    @Expose
    private String offerEligibility;

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

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getOfferTermsConditions() {
        return offerTermsConditions;
    }

    public void setOfferTermsConditions(String offerTermsConditions) {
        this.offerTermsConditions = offerTermsConditions;
    }

    public String getOfferEligibility() {
        return offerEligibility;
    }

    public void setOfferEligibility(String offerEligibility) {
        this.offerEligibility = offerEligibility;
    }

}
