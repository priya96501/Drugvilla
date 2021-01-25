package com.drugvilla.in.model.searching;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchingResponse extends BaseResponse {
    @SerializedName("search_data")
    @Expose
    private List<SearchingResultData> searchingResultDataList = null;

    public List<SearchingResultData> getSearchingResultDataList() {
        return searchingResultDataList;
    }

    public void setSearchingResultDataList(List<SearchingResultData> searchingResultDataList) {
        this.searchingResultDataList = searchingResultDataList;
    }
}
