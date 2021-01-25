package com.drugvilla.in.model.lab;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectLabResponse extends BaseResponse {
@SerializedName("data")
    @Expose
    private SelectLabData data;

    public SelectLabData getData() {
        return data;
    }

    public void setData(SelectLabData data) {
        this.data = data;
    }
}
