package com.drugvilla.in.model.orderCancellation;

import com.drugvilla.in.model.appointment.timeSlot.TimeSlotData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CancelllationResponse extends BaseResponse {

    @SerializedName("Reason_data")
    @Expose
    private List<CancellationData> cancellationData = null;

    public List<CancellationData> getCancellationData() {
        return cancellationData;
    }

    public void setCancellationData(List<CancellationData> cancellationData) {
        this.cancellationData = cancellationData;
    }
}
