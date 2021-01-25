package com.drugvilla.in.model.lab.test;

import com.drugvilla.in.model.Pagination;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestListingResponse extends BaseResponse {
    @SerializedName("test_data")
    @Expose
    private List<TestData> testDataList = null;
   /* @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }*/
    public List<TestData> getTestDataList() {
        return testDataList;
    }

    public void setTestDataList(List<TestData> testDataList) {
        this.testDataList = testDataList;
    }
}
