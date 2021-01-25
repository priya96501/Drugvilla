package com.drugvilla.in.model.blog;

import com.drugvilla.in.model.dashboard.CategoryData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BlogResponse extends BaseResponse {
    @SerializedName("blog_data")
    @Expose
    private List<BlogData> blogDataList = null;

    public List<BlogData> getBlogDataList() {
        return blogDataList;
    }

    public void setBlogDataList(List<BlogData> blogDataList) {
        this.blogDataList = blogDataList;
    }
}
