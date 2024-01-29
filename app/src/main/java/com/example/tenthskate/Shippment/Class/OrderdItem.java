package com.example.tenthskate.Shippment.Class;

public class OrderdItem {
    String productName;
    String productQuantity;

    public void OrderdItem(){

    }

    public OrderdItem(String productName, String productQuantity) {
        this.productName = productName;
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }
}
