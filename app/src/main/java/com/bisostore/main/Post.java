package com.bisostore.main;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by USER on 30/07/2017.
 */

public class Post implements Serializable{

    final static public String WEBSITE_URL="http://bisostore.com/mobile";
    final static public String POST_PATH="/get_post/";

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    private int post_id;
    private String images;

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    private String post_date;

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    private int remain;

    private ArrayList<String> inputs_name;

    public ArrayList<String> getInputs_value() {
        return inputs_value;
    }

    public void setInputs_value(ArrayList<String> inputs_value) {
        this.inputs_value = inputs_value;
    }

    public ArrayList<String> getInputs_name() {
        return inputs_name;
    }

    public void setInputs_name(ArrayList<String> inputs_name) {
        this.inputs_name = inputs_name;
    }

    private ArrayList<String> inputs_value;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    private String descrption;
    private String author_admin;
    private int published;
    private int post_visits;
    private int author_id;
    private int part_id;
    private String post_lang;
    private String country;
    private int period;
    private int price;
    private String coin;
    private int stateId;
    private int cityId;
    private int haiid;
    private String post_title;
    private String post_title_ar;
    private String post_title_en;
    private int distinctive;
    private int car_type;
    private int car_model;
    private int email_sent;
    private int distinctive_time;
    private java.util.Date distinctive_start_date;
    private int p_enabled;
    private int p_endtime_email;
    private int post_translate;
    private int post_edit_or_translate;
    private int checkpost;
    private String deleteresoan;
    private String parts_id;
    private String phonename1;
    private String phonename2;
    private String phonespec1;
    private String phonespec2;
    private String cars_type;
    private String post_lat;
    private String post_lan;
    private double post_rate;
    private int post_rate_count;
    private int checkadslongtimeemail1;
    private int checkadslongtimeemail2;
    private int checkadslongtimeemail3;
    private String post_email_exat;
    private String post_phone_exat;



    public String getAuthor_admin(){
        return author_admin;
    }

    public void setAuthor_admin(String author_admin){
        this.author_admin=author_admin;
    }

    public int getPublished(){
        return published;
    }

    public void setPublished(int published){
        this.published=published;
    }

    public int getPost_visits(){
        return post_visits;
    }

    public void setPost_visits(int post_visits){
        this.post_visits=post_visits;
    }

    public int getAuthor_id(){
        return author_id;
    }

    public void setAuthor_id(int author_id){
        this.author_id=author_id;
    }

    public int getPart_id(){
        return part_id;
    }

    public void setPart_id(int part_id){
        this.part_id=part_id;
    }

    public String getPost_lang(){
        return post_lang;
    }

    public void setPost_lang(String post_lang){
        this.post_lang=post_lang;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country=country;
    }

    public int getPeriod(){
        return period;
    }

    public void setPeriod(int period){
        this.period=period;
    }

    public int getPrice(){
        return price;
    }

    public void setPrice(int price){
        this.price=price;
    }

    public String getCoin(){
        return coin;
    }

    public void setCoin(String coin){
        this.coin=coin;
    }

    public int getStateid(){
        return stateId;
    }

    public void setStateid(int stateId){
        this.stateId=stateId;
    }

    public int getCityid(){
        return cityId;
    }

    public void setCityid(int cityId){
        this.cityId=cityId;
    }

    public int getHaiid(){
        return haiid;
    }

    public void setHaiid(int haiid){
        this.haiid=haiid;
    }
    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_title_en() {
        return post_title_en;
    }

    public void setPost_title_en(String post_title_en) {
        this.post_title_en = post_title_en;
    }

    public String getPost_title_ar() {
        return post_title_ar;
    }

    public void setPost_title_ar(String post_title_ar) {
        this.post_title_ar = post_title_ar;
    }
    public int getDistinctive(){
        return distinctive;
    }

    public void setDistinctive(int distinctive){
        this.distinctive=distinctive;
    }

    public int getCar_type(){
        return car_type;
    }

    public void setCar_type(int car_type){
        this.car_type=car_type;
    }

    public int getCar_model(){
        return car_model;
    }

    public void setCar_model(int car_model){
        this.car_model=car_model;
    }

    public int getEmail_sent(){
        return email_sent;
    }

    public void setEmail_sent(int email_sent){
        this.email_sent=email_sent;
    }

    public int getDistinctive_time(){
        return distinctive_time;
    }

    public void setDistinctive_time(int distinctive_time){
        this.distinctive_time=distinctive_time;
    }

    public java.util.Date getDistinctive_start_date(){
        return distinctive_start_date;
    }

    public void setDistinctive_start_date(java.util.Date distinctive_start_date){
        this.distinctive_start_date=distinctive_start_date;
    }

    public int getP_enabled(){
        return p_enabled;
    }

    public void setP_enabled(int p_enabled){
        this.p_enabled=p_enabled;
    }

    public int getP_endtime_email(){
        return p_endtime_email;
    }

    public void setP_endtime_email(int p_endtime_email){
        this.p_endtime_email=p_endtime_email;
    }

    public int getPost_translate(){
        return post_translate;
    }

    public void setPost_translate(int post_translate){
        this.post_translate=post_translate;
    }

    public int getPost_edit_or_translate(){
        return post_edit_or_translate;
    }

    public void setPost_edit_or_translate(int post_edit_or_translate){
        this.post_edit_or_translate=post_edit_or_translate;
    }

    public int getCheckpost(){
        return checkpost;
    }

    public void setCheckpost(int checkpost){
        this.checkpost=checkpost;
    }

    public String getDeleteresoan(){
        return deleteresoan;
    }

    public void setDeleteresoan(String deleteresoan){
        this.deleteresoan=deleteresoan;
    }

    public String getParts_id(){
        return parts_id;
    }

    public void setParts_id(String parts_id){
        this.parts_id=parts_id;
    }

    public String getPhonename1(){
        return phonename1;
    }

    public void setPhonename1(String phonename1){
        this.phonename1=phonename1;
    }

    public String getPhonename2(){
        return phonename2;
    }

    public void setPhonename2(String phonename2){
        this.phonename2=phonename2;
    }

    public String getPhonespec1(){
        return phonespec1;
    }

    public void setPhonespec1(String phonespec1){
        this.phonespec1=phonespec1;
    }

    public String getPhonespec2(){
        return phonespec2;
    }

    public void setPhonespec2(String phonespec2){
        this.phonespec2=phonespec2;
    }

    public String getCars_type(){
        return cars_type;
    }

    public void setCars_type(String cars_type){
        this.cars_type=cars_type;
    }

    public String getPost_lat(){
        return post_lat;
    }

    public void setPost_lat(String post_lat){
        this.post_lat=post_lat;
    }

    public String getPost_lan(){
        return post_lan;
    }

    public void setPost_lan(String post_lan){
        this.post_lan=post_lan;
    }

    public double getPost_rate(){
        return post_rate;
    }

    public void setPost_rate(double post_rate){
        this.post_rate=post_rate;
    }

    public int getPost_rate_count(){
        return post_rate_count;
    }

    public void setPost_rate_count(int post_rate_count){
        this.post_rate_count=post_rate_count;
    }

    public int getCheckadslongtimeemail1(){
        return checkadslongtimeemail1;
    }

    public void setCheckadslongtimeemail1(int checkadslongtimeemail1){
        this.checkadslongtimeemail1=checkadslongtimeemail1;
    }

    public int getCheckadslongtimeemail2(){
        return checkadslongtimeemail2;
    }

    public void setCheckadslongtimeemail2(int checkadslongtimeemail2){
        this.checkadslongtimeemail2=checkadslongtimeemail2;
    }

    public int getCheckadslongtimeemail3(){
        return checkadslongtimeemail3;
    }

    public void setCheckadslongtimeemail3(int checkadslongtimeemail3){
        this.checkadslongtimeemail3=checkadslongtimeemail3;
    }

    public String getPost_email_exat(){
        return post_email_exat;
    }

    public void setPost_email_exat(String post_email_exat){
        this.post_email_exat=post_email_exat;
    }

    public String getPost_phone_exat(){
        return post_phone_exat;
    }

    public void setPost_phone_exat(String post_phone_exat){
        this.post_phone_exat=post_phone_exat;
    }
}