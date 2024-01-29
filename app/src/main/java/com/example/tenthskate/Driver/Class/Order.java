package com.example.tenthskate.Driver.Class;

public class Order {


    private String orderid;
    private String uId,key,status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public Order(){

    }

    public Order(String orderid,String uId,String key,String status) {

        this.orderid = orderid;
        this.uId = uId;
        this.key = key;
        this.status=status;
    }


}
