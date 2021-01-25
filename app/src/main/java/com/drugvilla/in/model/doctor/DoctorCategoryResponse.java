package com.drugvilla.in.model.doctor;

import com.drugvilla.in.model.Pagination;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorCategoryResponse extends BaseResponse {

    @SerializedName("doctor_category_data")
    @Expose
    private List<DoctorCategoryData> doctorCategoryDataList = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<DoctorCategoryData> getDoctorCategoryDataList() {
        return doctorCategoryDataList;
    }

    public void setDoctorCategoryDataList(List<DoctorCategoryData> doctorCategoryDataList) {
        this.doctorCategoryDataList = doctorCategoryDataList;
    }
}
