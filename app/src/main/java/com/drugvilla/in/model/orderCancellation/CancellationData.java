package com.drugvilla.in.model.orderCancellation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancellationData {

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("reason_id")
    @Expose
    private String reason_id;

    private boolean selected;


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }
}
