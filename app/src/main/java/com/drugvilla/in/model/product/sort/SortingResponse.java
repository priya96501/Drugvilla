package com.drugvilla.in.model.product.sort;

import com.drugvilla.in.model.reminder.ReminderData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SortingResponse extends BaseResponse {
    @SerializedName("sorting_data")
    @Expose
    private List<SortingData> sortingDataList = null;

    public List<SortingData> getSortingDataList() {
        return sortingDataList;
    }

    public void setSortingDataList(List<SortingData> sortingDataList) {
        this.sortingDataList = sortingDataList;
    }
}
