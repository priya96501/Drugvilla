package com.drugvilla.in.model.patient;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatientBaseResponse extends BaseResponse {
    @SerializedName("patient_data")
    @Expose
    private List<PatientData> patientData = null;

    public List<PatientData> getPatientData() {
        return patientData;
    }

    public void setPatientData(List<PatientData> patientData) {
        this.patientData = patientData;
    }
}
