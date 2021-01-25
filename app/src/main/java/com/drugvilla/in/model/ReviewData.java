package com.drugvilla.in.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewData {
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_review")
    @Expose
    private String userReview;
    @SerializedName("user_rating")
    @Expose
    private String userRating;
    @SerializedName("user_id")
    @Expose
    private String user_id;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
