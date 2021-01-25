package com.drugvilla.in.model.dashboard.labDashboard;

import com.drugvilla.in.model.lab.labs.LabData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LabDashboardResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<LabDashboardData> dataList = null;

    public List<LabDashboardData> getDataList() {
        return dataList;
    }

    public void setDataList(List<LabDashboardData> dataList) {
        this.dataList = dataList;
    }
}
