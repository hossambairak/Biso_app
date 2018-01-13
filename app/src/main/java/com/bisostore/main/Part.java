package com.bisostore.main;

/**
 * Created by USER on 30/07/2017.
 */

public class Part {

    public static final String SUPPART="/get_subparts/";
    private String name_ar;
    private String name_en;
    private int part_level;

    public int getCount_post() {
        return count_post;
    }

    public void setCount_post(int count_post) {
        this.count_post = count_post;
    }

    private int count_post;
    private int belong;
    private String icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public int getPart_level() {
        return part_level;
    }

    public void setPart_level(int part_level) {
        this.part_level = part_level;
    }

    public int getBelong() {
        return belong;
    }

    public void setBelong(int belong) {
        this.belong = belong;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }



}
