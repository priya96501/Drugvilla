package com.drugvilla.in.model.dashboard;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse extends BaseResponse {
    @SerializedName("category_data")
    @Expose
    private List<CategoryData> categoryDataList = null;

    public List<CategoryData> getCategoryDataList() {
        return categoryDataList;
    }

    public void setCategoryDataList(List<CategoryData> categoryDataList) {
        this.categoryDataList = categoryDataList;
    }
}
