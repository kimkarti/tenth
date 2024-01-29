package com.example.tenthskate.Clients.ModelClass;

public class Order {
    private String fullName;
    private String mobile;
    private String address;
    private String value;
    private String orderID;
    private String date;
    private String time;
    private String status;
    private String uId;

    public String getPesa() {
        return pesa;
    }

    public void setPesa(String pesa) {
        this.pesa = pesa;
    }

    String pesa;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Order(){

    }

    public Order(String fullName, String mobile, String address, String value, String orderID, String date, String time, String status, String uId, String pesa) {
        this.fullName = fullName;
        this.mobile = mobile;
        this.address = address;
        this.value = value;
        this.orderID = orderID;
        this.date = date;
        this.time = time;
        this.status = status;
        this.uId = uId;
        this.pesa=pesa;

    }

}
