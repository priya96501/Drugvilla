package com.drugvilla.in.model.address;

import com.drugvilla.in.model.patient.PatientData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressBaseResponse extends BaseResponse {
    @SerializedName("address_data")
    @Expose
    private List<AddressData> addressData = null;

    public List<AddressData> getAddressData() {
        return addressData;
    }

    public void setAddressData(List<AddressData> addressData) {
        this.addressData = addressData;
    }


}
