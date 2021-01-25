package com.drugvilla.in.model.blog.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReplyData {

    @SerializedName("comment_id")
    @Expose
    private String comment_id;
    @SerializedName("reply_id")
    @Expose
    private String reply_id;
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
    @SerializedName("reply_to")
    @Expose
    private String reply_to;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("blog_id")
    @Expose
    private String blog_id;

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
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

    public String getReply_to() {
        return reply_to;
    }

    public void setReply_to(String reply_to) {
        this.reply_to = reply_to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
