package com.drugvilla.in.model.lab.labs;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabResponse extends BaseResponse {
    @SerializedName("lab_data")
    @Expose
    private LabDetail labDetail;

    public LabDetail getLabDetail() {
        return labDetail;
    }

    public void setLabDetail(LabDetail labDetail) {
        this.labDetail = labDetail;
    }
}
