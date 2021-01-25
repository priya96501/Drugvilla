package com.drugvilla.in.model.blog.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentsData {
    @SerializedName("comment_id")
    @Expose
    private String comment_id;

    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("is_update_available")
    @Expose
    private String is_update_available;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("reply_data")
    @Expose
    List<ReplyData> replyDataList = null;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getIs_update_available() {
        return is_update_available;
    }

    public void setIs_update_available(String is_update_available) {
        this.is_update_available = is_update_available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReplyData> getReplyDataList() {
        return replyDataList;
    }

    public void setReplyDataList(List<ReplyData> replyDataList) {
        this.replyDataList = replyDataList;
    }
}
