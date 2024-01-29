package com.example.tenthskate.Clients.ModelClass;

public class CartUpload {

    private String productName,category;
    private String productDescription;
    private String productPrice;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String productID;
    private String productImageUrl;
    private String productQuantity="1";
    private String productCuttedPrice;

    public CartUpload(){
    }

    public CartUpload(String productName, String productDescription, String productPrice, String productID, String productImageUrl, String productQuantity, String productCuttedPrice, String category) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productID = productID;
        this.productImageUrl = productImageUrl;
        this.productQuantity = productQuantity;
        this.productCuttedPrice = productCuttedPrice;
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductCuttedPrice() {
        return productCuttedPrice;
    }

    public void setProductCuttedPrice(String productCuttedPrice) {
        this.productCuttedPrice = productCuttedPrice;
    }
}
