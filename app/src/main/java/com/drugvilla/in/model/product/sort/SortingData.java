package com.drugvilla.in.model.product.sort;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SortingData {

    @SerializedName("sorting_id")
    @Expose
    private String sortingId;
    @SerializedName("sorting_type")
    @Expose
    private String sortingType;
    @SerializedName("sorting_image")
    @Expose
    private String sortingImage;

    public String getSortingImage() {
        return sortingImage;
    }

    public void setSortingImage(String sortingImage) {
        this.sortingImage = sortingImage;
    }

    public String getSortingId() {
        return sortingId;
    }

    public void setSortingId(String sortingId) {
        this.sortingId = sortingId;
    }

    public String getSortingType() {
        return sortingType;
    }

    public void setSortingType(String sortingType) {
        this.sortingType = sortingType;
    }
}
