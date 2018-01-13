package com.bisostore.main;

/**
 * Created by USER on 31/07/2017.
 */

public class Country {

    final static public String WEBSITE_URL="http://bisostore.com/mobile";
    final static public String countryies_PATH="/get_countries/";

    public int getCountryid() {
        return countryid;
    }

    public void setCountryid(int countryid) {
        this.countryid = countryid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getShown() {
        return shown;
    }

    public void setShown(int shown) {
        this.shown = shown;
    }

    public Country(int countryid, String id, String name_ar, String name_en, int shown) {
        this.countryid = countryid;
        this.id = id;
        this.name_ar = name_ar;
        this.name_en = name_en;
        this.shown = shown;
    }

    private int countryid;
    private String id;
    private String name_ar;
    private String name_en;
    private int shown;

}
