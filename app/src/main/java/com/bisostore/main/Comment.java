package com.bisostore.main;

/**
 * Created by USER on 20/10/2017.
 */

public class Comment {

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_content() {
        return Comment_content;
    }

    public void setComment_content(String comment_content) {
        Comment_content = comment_content;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    private int comment_id;

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    private String u_name;

    public Comment(String u_name, String comment_content, String comment_date) {
        this.u_name = u_name;
        Comment_content = comment_content;
        this.comment_date = comment_date;
    }

    private String Comment_content;
    private String comment_date;

}
