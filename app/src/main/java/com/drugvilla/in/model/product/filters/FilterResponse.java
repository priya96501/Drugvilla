package com.drugvilla.in.model.product.filters;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<FilterCategoryData> filterCategoryDataList = null;

    public List<FilterCategoryData> getData() {
        return filterCategoryDataList;
    }

    public void setData(List<FilterCategoryData> filterCategoryDataList) {
        this.filterCategoryDataList = filterCategoryDataList;
    }
}
