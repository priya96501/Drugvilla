package com.drugvilla.in.model.product.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductDetail {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("provided_by")
    @Expose
    private String providedBy;
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
    @SerializedName("offer")
    @Expose
    private ArrayList<String> offer = null;
    @SerializedName("images")
    @Expose
    private ArrayList<String> images = null;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("supplier_address")
    @Expose
    private String supplierAddress;
    @SerializedName("sililar_product_data")
    @Expose
    private List<ProductData> sililarProductData = null;

    public ArrayList<String> getOffer() {
        return offer;
    }

    public void setOffer(ArrayList<String> offer) {
        this.offer = offer;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProvidedBy() {
        return providedBy;
    }

    public void setProvidedBy(String providedBy) {
        this.providedBy = providedBy;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public List<ProductData> getSililarProductData() {
        return sililarProductData;
    }

    public void setSililarProductData(List<ProductData> sililarProductData) {
        this.sililarProductData = sililarProductData;
    }

}


