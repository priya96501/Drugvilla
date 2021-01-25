package com.drugvilla.in.model.lab.test;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestResponse extends BaseResponse {
    @SerializedName("test_detail")
    @Expose
    private TestDetail testDetail;

    public TestDetail getTestDetail() {
        return testDetail;
    }

    public void setTestDetail(TestDetail testDetail) {
        this.testDetail = testDetail;
    }
}
