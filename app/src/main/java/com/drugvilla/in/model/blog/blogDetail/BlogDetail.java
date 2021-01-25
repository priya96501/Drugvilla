package com.drugvilla.in.model.blog.blogDetail;

import com.drugvilla.in.model.blog.BlogData;
import com.drugvilla.in.model.dashboard.HomeSubData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BlogDetail {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("blog_id")
    @Expose
    private String blogId;
    @SerializedName("is_favourite")
    @Expose
    private String isFavourite;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("blog_by")
    @Expose
    private String blogBy;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("blog_data")
    @Expose
    private List<HomeSubData> relatedBlogDataList = null;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBlogBy() {
        return blogBy;
    }

    public void setBlogBy(String blogBy) {
        this.blogBy = blogBy;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<HomeSubData> getRelatedBlogDataList() {
        return relatedBlogDataList;
    }

    public void setRelatedBlogDataList(List<HomeSubData> relatedBlogDataList) {
        this.relatedBlogDataList = relatedBlogDataList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
