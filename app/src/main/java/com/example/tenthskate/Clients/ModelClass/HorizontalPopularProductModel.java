package com.example.tenthskate.Clients.ModelClass;

public class HorizontalPopularProductModel {
    private String productImage;
    private String productTitle;
    private String productCuttedPrice;
    private String productPrice;
    private String productID;

    public HorizontalPopularProductModel(){

    }

    public HorizontalPopularProductModel(String productImage, String productTitle, String productCuttedPrice, String productPrice, String productID) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productCuttedPrice = productCuttedPrice;
        this.productPrice = productPrice;
        this.productID = productID;
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

    public String getProductCuttedPrice() {
        return productCuttedPrice;
    }

    public void setProductCuttedPrice(String productCuttedPrice) {
        this.productCuttedPrice = productCuttedPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
