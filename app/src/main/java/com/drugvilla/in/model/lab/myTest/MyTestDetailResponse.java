package com.drugvilla.in.model.lab.myTest;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyTestDetailResponse extends BaseResponse {

    @SerializedName("order_detail")
    @Expose
    private MyTestData myTestData;

    public MyTestData getMyTestData() {
        return myTestData;
    }

    public void setMyTestData(MyTestData myTestData) {
        this.myTestData = myTestData;
    }
}
