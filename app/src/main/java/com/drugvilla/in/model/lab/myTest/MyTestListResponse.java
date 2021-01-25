package com.drugvilla.in.model.lab.myTest;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyTestListResponse extends BaseResponse {

    @SerializedName("order_detail")
    @Expose
    private List<MyTestData> myTestData = null;

     /* @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }*/

    public List<MyTestData> getMyTestData() {
        return myTestData;
    }

    public void setMyTestData(List<MyTestData> myTestData) {
        this.myTestData = myTestData;
    }
}