package com.drugvilla.in.model.doctor;

import com.drugvilla.in.model.Pagination;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorListResponse extends BaseResponse {

    @SerializedName("doctors_data")
    @Expose
    private List<DoctorData> doctorDataList = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<DoctorData> getDoctorDataList() {
        return doctorDataList;
    }

    public void setDoctorDataList(List<DoctorData> doctorDataList) {
        this.doctorDataList = doctorDataList;
    }
}
