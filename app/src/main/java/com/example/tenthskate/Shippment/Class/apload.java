package com.example.tenthskate.Shippment.Class;


public class apload {

    private String productName;
    private String productCode;
    private String productDescription;
    private String productImageUrl;
    private String productCategory;
    private String productPrice;
    private String ProductCuttedPrice;
    private String productID;
    private String searchableName;

    public apload(){
    }

    public apload(String productName,String productDescription,  String productCategory, String productPrice,  String productID, String searchableName) {
        this.productName = productName;
        this.productCode = productCode;
        this.productDescription = productDescription;
        this.productImageUrl = productImageUrl;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.ProductCuttedPrice = ProductCuttedPrice;
        this.productID = productID;
        this.searchableName = searchableName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCuttedPrice() {
        return ProductCuttedPrice;
    }

    public void setProductCuttedPrice(String productCuttedPrice) {
        this.ProductCuttedPrice = productCuttedPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSearchableName() {
        return searchableName;
    }

    public void setSearchableName(String searchableName) {
        this.searchableName = searchableName;
    }
}
