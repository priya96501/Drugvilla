package com.drugvilla.in.model.doctor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorCategoryData {
    @SerializedName("doctor_category_id")
    @Expose
    private String categoryId;
    @SerializedName("doctor_category_name")
    @Expose
    private String categoryName;
    @SerializedName("doctor_category_image")
    @Expose
    private String categoryImage;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }
}
