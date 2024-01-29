package com.example.tenthskate.driverspecs.Class;

public class Order {
    private String name;
    private String phone;
    private String license;
    private String email;

    public void setPhone(String mobile) {
        this.phone = phone;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }





    public Order(){

    }

    public Order(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }


}
