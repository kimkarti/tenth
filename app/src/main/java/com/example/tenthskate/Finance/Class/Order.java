package com.example.tenthskate.Finance.Class;

import com.example.tenthskate.stockinventor.Class.Upload;

import java.util.List;

public class Order {
    private String fullName;
    private String mobile;
    private String address;
    private String value;
    private String orderID;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String date;
    private String email;
    public String getPesa() {
        return pesa;
    }

    public void setPesa(String pesa) {
        this.pesa = pesa;
    }

    private String time;
    private String status;
    private String pesa;
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



    public Order(){

    }

    public Order(String fullName, String mobile, String address, String value, String orderID, String date, String time, String status, List<Upload> items,String pesa,String email) {
        this.fullName = fullName;
        this.mobile = mobile;
        this.address = address;
        this.value = value;
        this.orderID = orderID;
        this.date = date;
        this.time = time;
        this.status = status;
        this.items = items;
        this.pesa = pesa;
        this.email = email;
    }


}
