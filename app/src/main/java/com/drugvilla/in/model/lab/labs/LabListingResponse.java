package com.drugvilla.in.model.lab.labs;

import com.drugvilla.in.model.Pagination;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LabListingResponse  extends BaseResponse {
    @SerializedName("lab_data")
    @Expose
    private List<LabData> labDataList = null;
   /* @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
*/
    public List<LabData> getLabDataList() {
        return labDataList;
    }

    public void setLabDataList(List<LabData> labDataList) {
        this.labDataList = labDataList;
    }
}
