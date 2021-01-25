package com.drugvilla.in.model.product;

import com.drugvilla.in.model.dashboard.CategoryData;
import com.drugvilla.in.model.product.category.ProductSubCategoryData;
import com.drugvilla.in.model.product.productdetail.ProductData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductCategoryDetailPageData {
    @SerializedName("product_category_id")
    @Expose
    private String CategoryId;

    @SerializedName("product_category_name")
    @Expose
    private String CategoryName;

    @SerializedName("product_category_banner")
    @Expose
    private String CategoryBanner;
    @SerializedName("product_sub_category_data")
    @Expose
    private List<CategoryData> productSubCategoryDataList = null;

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

    public String getCategoryBanner() {
        return CategoryBanner;
    }

    public void setCategoryBanner(String categoryBanner) {
        CategoryBanner = categoryBanner;
    }

    public List<CategoryData> getProductSubCategoryDataList() {
        return productSubCategoryDataList;
    }

    public void setProductSubCategoryDataList(List<CategoryData> productSubCategoryDataList) {
        this.productSubCategoryDataList = productSubCategoryDataList;
    }
}
