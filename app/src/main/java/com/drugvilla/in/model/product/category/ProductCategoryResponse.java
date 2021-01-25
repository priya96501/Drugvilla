package com.drugvilla.in.model.product.category;

import com.drugvilla.in.model.reminder.ReminderData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductCategoryResponse extends BaseResponse {

    @SerializedName("product_category_data")
    @Expose
    private List<ProductCategoryData> productCategoryDataList = null;

    public List<ProductCategoryData> getProductCategoryDataList() {
        return productCategoryDataList;
    }

    public void setProductCategoryDataList(List<ProductCategoryData> productCategoryDataList) {
        this.productCategoryDataList = productCategoryDataList;
    }
}
