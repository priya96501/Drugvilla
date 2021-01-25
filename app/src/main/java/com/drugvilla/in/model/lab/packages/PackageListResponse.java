package com.drugvilla.in.model.lab.packages;

import com.drugvilla.in.model.Pagination;
import com.drugvilla.in.model.lab.labs.LabData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackageListResponse extends BaseResponse {

    @SerializedName("package_data")
    @Expose
    private List<PackageData> packageDataList = null;
   /* @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }*/
    public List<PackageData> getPackageDataList() {
        return packageDataList;
    }

    public void setPackageDataList(List<PackageData> packageDataList) {
        this.packageDataList = packageDataList;
    }
}
