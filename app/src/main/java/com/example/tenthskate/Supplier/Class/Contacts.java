package com.example.tenthskate.Supplier.Class;

public class Contacts {

    private String name;
    private String Uid;

    public  Contacts(){

    }

    public Contacts(String name, String uid) {
        this.name = name;
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
