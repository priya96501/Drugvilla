package com.drugvilla.in.model.blog.comments;

import com.drugvilla.in.model.blog.BlogData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentResponse extends BaseResponse {
    @SerializedName("comment_data")
    @Expose
    private List<CommentsData> commentsDataList = null;

    public List<CommentsData> getCommentsDataList() {
        return commentsDataList;
    }

    public void setCommentsDataList(List<CommentsData> commentsDataList) {
        this.commentsDataList = commentsDataList;
    }
}
