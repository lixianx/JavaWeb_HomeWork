package com.oa.learn.bean;

import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

public class Goods {
    private String no;
    private String name;
    private String producer;
    private String date;
    private String model;
    private String buyPrice;
    private String retailPrice;
    private String amount;
    private String image;
    private int times;

    public Goods() {
    }

    public Goods(String no, String name, String producer, String date, String model, String buyPrice, String retailPrice, String amount, String image, int times) {
        this.no = no;
        this.name = name;
        this.producer = producer;
        this.date = date;
        this.model = model;
        this.buyPrice = buyPrice;
        this.retailPrice = retailPrice;
        this.amount = amount;
        this.image = image;
        this.times = times;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
