package com.drugvilla.in.model.order;

import com.drugvilla.in.model.PrescriptionData;
import com.drugvilla.in.model.order.myOrder.MyOrderListData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PrescriptionResponse extends BaseResponse {
    @SerializedName("prescriptions")
    @Expose
    private List<PrescriptionData> prescriptionDataList = null;

    public List<PrescriptionData> getPrescriptionDataList() {
        return prescriptionDataList;
    }

    public void setPrescriptionDataList(List<PrescriptionData> prescriptionDataList) {
        this.prescriptionDataList = prescriptionDataList;
    }
}
