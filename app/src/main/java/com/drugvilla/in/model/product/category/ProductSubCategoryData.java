package com.drugvilla.in.model.product.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductSubCategoryData {

    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;

    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;

    @SerializedName("sub_category_image")
    @Expose
    private String subCategoryImage;

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getSubCategoryImage() {
        return subCategoryImage;
    }

    public void setSubCategoryImage(String subCategoryImage) {
        this.subCategoryImage = subCategoryImage;
    }
}
