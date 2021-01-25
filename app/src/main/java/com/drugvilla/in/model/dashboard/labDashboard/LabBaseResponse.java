package com.drugvilla.in.model.dashboard.labDashboard;

import com.drugvilla.in.model.dashboard.HomeSubData;
import com.drugvilla.in.model.lab.labs.LabData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LabBaseResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<HomeSubData> labDataList = null;

    public List<HomeSubData> getLabDataList() {
        return labDataList;
    }

    public void setLabDataList(List<HomeSubData> labDataList) {
        this.labDataList = labDataList;
    }
}
