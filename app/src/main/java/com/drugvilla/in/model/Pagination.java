package com.drugvilla.in.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("current_page")
    @Expose
    private String currentPage;

    @SerializedName("total_pages")
    @Expose
    private String maxPage;

    public void setMaxPage(String maxPage) {
        this.maxPage = maxPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

}
