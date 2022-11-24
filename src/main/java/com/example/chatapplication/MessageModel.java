package com.example.chatapplication;

public class MessageModel {
    private String message;
    private Boolean isItFrom;

    public MessageModel(String message, Boolean isItFrom) {
        this.message = message;
        this.isItFrom = isItFrom;
    }

    public MessageModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getItFrom() {
        return isItFrom;
    }

    public void setItFrom(Boolean itFrom) {
        isItFrom = itFrom;
    }
}
