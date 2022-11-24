package com.example.chatapplication;

public class Module {
    String message,senderName,time,profilePhoto;

    public Module(String message, String senderName, String time, String profilePhoto) {
        this.message = message;
        this.senderName = senderName;
        this.time = time;
        this.profilePhoto = profilePhoto;
    }

    public Module() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
