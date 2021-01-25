package com.drugvilla.in.model.dashboard;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeData  {
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("is_view_all")
    @Expose
    private String isViewAll;
    @SerializedName("api_type")
    @Expose
    private String apiType;
    @SerializedName("is_rating")
    @Expose
    private String isRating;
    @SerializedName("is_discount")
    @Expose
    private String isDiscount;
    @SerializedName("sub_data")
    @Expose
    private List<HomeSubData> subData = null;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsViewAll() {
        return isViewAll;
    }

    public void setIsViewAll(String isViewAll) {
        this.isViewAll = isViewAll;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public List<HomeSubData> getSubData() {
        return subData;
    }

    public void setSubData(List<HomeSubData> subData) {
        this.subData = subData;
    }

    public String getIsRating() {
        return isRating;
    }

    public void setIsRating(String isRating) {
        this.isRating = isRating;
    }

    public String getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(String isDiscount) {
        this.isDiscount = isDiscount;
    }
}
