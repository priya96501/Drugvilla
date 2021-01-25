package com.drugvilla.in.model.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SelectedItem {

    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_image")
    @Expose
    private String itemImage;
    @SerializedName("item_type")
    @Expose
    private String itemType;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("mrp")
    @Expose
    private String mrp;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("provided_by")
    @Expose
    private String provided_by;
    @SerializedName("item_quantity")
    @Expose
    private String item_quantity;

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

    public String getProvided_by() {
        return provided_by;
    }

    public void setProvided_by(String provided_by) {
        this.provided_by = provided_by;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
