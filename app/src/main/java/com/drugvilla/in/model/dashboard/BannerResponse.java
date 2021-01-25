package com.drugvilla.in.model.dashboard;

import com.drugvilla.in.model.lab.labs.LabData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerResponse extends BaseResponse {

    @SerializedName("banner_data")
    @Expose
    private List<BannerData> bannerDataList = null;

    public List<BannerData> getBannerDataList() {
        return bannerDataList;
    }

    public void setBannerDataList(List<BannerData> bannerDataList) {
        this.bannerDataList = bannerDataList;
    }
}
