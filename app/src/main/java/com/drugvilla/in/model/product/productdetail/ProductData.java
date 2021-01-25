package com.drugvilla.in.model.product.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductData {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("is_wishlisted")
    @Expose
    private String isWishlisted;
    @SerializedName("provided_by")
    @Expose
    private String providedBy;

    @SerializedName("product_type")
    @Expose
    private String productType;

    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("mrp")
    @Expose
    private String mrp;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProvidedBy() {
        return providedBy;
    }

    public void setProvidedBy(String providedBy) {
        this.providedBy = providedBy;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsWishlisted() {
        return isWishlisted;
    }

    public void setIsWishlisted(String isWishlisted) {
        this.isWishlisted = isWishlisted;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
