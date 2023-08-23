package com.oa.learn.bean;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;

import java.util.Date;

public class User implements HttpSessionBindingListener {
    private String id;
    private String username;
    private String password;
    private Level level;
    private String phone;
    private String email;
    private Date startDate;
    private String role;
    private double totalConsume;

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        //用户登录
        ServletContext application = event.getSession().getServletContext();
        Object onlineCount = application.getAttribute("onlineCount");
        if (onlineCount == null) {
            application.setAttribute("onlineCount", 1);
        }
        else application.setAttribute("onlineCount", (Integer)onlineCount + 1);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        //用户退出
        ServletContext application = event.getSession().getServletContext();
        Object onlineCount = application.getAttribute("onlineCount");
        application.setAttribute("onlineCount", (Integer)onlineCount - 1);
    }

    public User() {
    }

    public User(String id, String username, String password, Level level, String phone, String email, Date startDate, String role, double totalConsume) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.level = level;
        this.phone = phone;
        this.email = email;
        this.startDate = startDate;
        this.role = role;
        this.totalConsume = totalConsume;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public double getTotalConsume() {
        return totalConsume;
    }

    public void setTotalConsume(double totalConsume) {
        this.totalConsume = totalConsume;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", level=" + level +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", startDate=" + startDate +
                ", role='" + role + '\'' +
                ", totalConsume=" + totalConsume +
                '}';
    }
}
