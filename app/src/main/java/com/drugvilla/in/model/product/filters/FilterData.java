package com.drugvilla.in.model.product.filters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterData {
    @SerializedName("filter_name")
    @Expose
    private String filterName;
    @SerializedName("filter_id")
    @Expose
    private Integer filterId;
    @SerializedName("filter_count")
    @Expose
    private String filterCount;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Integer getFilterId() {
        return filterId;
    }

    public void setFilterId(Integer filterId) {
        this.filterId = filterId;
    }

    public String getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(String filterCount) {
        this.filterCount = filterCount;
    }
}
