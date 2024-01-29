package com.example.tenthskate.Clients.ModelClass;

public class Messages {
    private String from, message, to, messageID, time, date;

    public Messages(){
    }

    public Messages(String from, String message, String to, String messageID, String time, String date) {
        this.from = from;
        this.message = message;
        this.to = to;
        this.messageID = messageID;
        this.time = time;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
