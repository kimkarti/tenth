package com.example.tenthskate.Driver.Class;

public class MyOrderItemModel {

    private String productImage;
    private float rating=0f;
    private String productTitle;
    private String orderDate;
    private String productID;
    private String productUniqueID;
    private String orderStatus;
    private String orderId;

    public MyOrderItemModel(){

    }

    public MyOrderItemModel(String productImage, int rating, String productTitle, String orderDate, String productID, String productUniqueID, String orderStatus, String orderId) {
        this.productImage = productImage;
        this.rating = rating;
        this.productTitle = productTitle;
        this.orderDate = orderDate;
        this.productID = productID;
        this.productUniqueID = productUniqueID;
        this.orderStatus = orderStatus;
        this.orderId = orderId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
    public String getProductUniqueID() {
        return productUniqueID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setProductUniqueID(String productUniqueID) {
        this.productUniqueID = productUniqueID;
    }

    public String getOrderID() {
        return orderId;
    }

    public void setOrderID(String orderId) {
        this.orderId = orderId;
    }
}
