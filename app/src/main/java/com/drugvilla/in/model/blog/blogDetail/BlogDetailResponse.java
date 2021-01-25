package com.drugvilla.in.model.blog.blogDetail;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlogDetailResponse extends BaseResponse {
    @SerializedName("blog_data")
    @Expose
    private BlogDetail blogDetail;

    public BlogDetail getBlogDetail() {
        return blogDetail;
    }

    public void setBlogDetail(BlogDetail blogDetail) {
        this.blogDetail = blogDetail;
    }
}
