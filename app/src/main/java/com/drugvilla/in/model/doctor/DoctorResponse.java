package com.drugvilla.in.model.doctor;

import com.drugvilla.in.model.ReviewData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorResponse extends BaseResponse {

    @SerializedName("doctor_data")
    @Expose
    private DoctorData DoctorData;

    public com.drugvilla.in.model.doctor.DoctorData getDoctorData() {
        return DoctorData;
    }

    public void setDoctorData(com.drugvilla.in.model.doctor.DoctorData doctorData) {
        DoctorData = doctorData;
    }
}
