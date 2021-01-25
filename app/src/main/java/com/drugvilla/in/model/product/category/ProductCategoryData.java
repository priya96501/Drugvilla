package com.drugvilla.in.model.product.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryData {

    @SerializedName("product_category_id")
    @Expose
    private String CategoryId;

    @SerializedName("product_category_name")
    @Expose
    private String CategoryName;

    @SerializedName("product_category_image")
    @Expose
    private String CategoryImage;

    @SerializedName("product_sub_category_data")
    @Expose
    private ArrayList<String> productSubCategoryDataList = null;

    public ArrayList<String> getProductSubCategoryDataList() {
        return productSubCategoryDataList;
    }

    public void setProductSubCategoryDataList(ArrayList<String> productSubCategoryDataList) {
        this.productSubCategoryDataList = productSubCategoryDataList;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }


}
