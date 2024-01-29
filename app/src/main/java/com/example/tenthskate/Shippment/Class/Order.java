package com.example.tenthskate.Shippment.Class;

import java.util.List;

public class Order {
    private String fullName, uId;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    private String mobile;
    private String address;



    private String value;
    private String orderID;
    private String date;
    private String time;
    private String status;
    private List<Upload> items;

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

    public List<Upload> getItems() {
        return items;
    }

    public void setItems(List<Upload> items) {
        this.items = items;
    }



    public  Order(){

    }

    public Order(String fullName, String mobile, String address, String value, String orderID, String date, String time, String status, List<Upload> items, String uId) {
        this.fullName = fullName;
        this.mobile = mobile;
        this.address = address;
        this.value = value;
        this.orderID = orderID;
        this.date = date;
        this.time = time;
        this.status = status;
        this.items = items;
        this.uId = uId;
    }


}
