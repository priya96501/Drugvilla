package com.drugvilla.in.model.lab.packages;

import com.drugvilla.in.model.lab.labs.LabDetail;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageResponse extends BaseResponse {
    @SerializedName("package_data")
    @Expose
    private PackageDetail packageDetail;

    public PackageDetail getPackageDetail() {
        return packageDetail;
    }

    public void setPackageDetail(PackageDetail packageDetail) {
        this.packageDetail = packageDetail;
    }
}
