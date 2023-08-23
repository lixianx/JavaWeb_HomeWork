package com.oa.learn.bean;

import java.util.Arrays;
import java.util.Date;

public class GoodInCar {
    private String id;
    private String name;
    private int num;
    private int maxNum;
    private byte[] image;
    private double price;
    private Date date;

    public GoodInCar() {
    }


    public GoodInCar(String id, String name, int num, int maxNum, byte[] image, double price) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.maxNum = maxNum;
        this.image = image;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
