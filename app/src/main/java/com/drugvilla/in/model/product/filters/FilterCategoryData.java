package com.drugvilla.in.model.product.filters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterCategoryData {

    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("filter_data")
    @Expose
    private List<FilterData> filterDataList = null;
    @SerializedName("category_count")
    @Expose
    private String categoryCount;

    private boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public List<FilterData> getFilterDataList() {
        return filterDataList;
    }

    public void setFilterDataList(List<FilterData> filterDataList) {
        this.filterDataList = filterDataList;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }


    public String getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(String categoryCount) {
        this.categoryCount = categoryCount;
    }
}
