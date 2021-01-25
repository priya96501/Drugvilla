package com.drugvilla.in.model.product.medicinedetail;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicineResponse extends BaseResponse {

    @SerializedName("product_data")
    @Expose
    private MedicineDetail medicineData;

    public MedicineDetail getMedicineData() {
        return medicineData;
    }

    public void setMedicineData(MedicineDetail medicineData) {
        this.medicineData = medicineData;
    }
}
