package com.oscarlolero.firebaserealtimechat.pojos;

public class User {

    private String id;
    private String name;
    private String mail;
    private String picture;
    private String status;
    private String date;
    private String time;
    private int requests;
    private int newMessage;

    public User() {
    }

    public User(String id, String name, String mail, String picture, String status, String date, String time, int requests, int newMessage) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.picture = picture;
        this.status = status;
        this.date = date;
        this.time = time;
        this.requests = requests;
        this.newMessage = newMessage;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public int getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(int newMessage) {
        this.newMessage = newMessage;
    }
}
