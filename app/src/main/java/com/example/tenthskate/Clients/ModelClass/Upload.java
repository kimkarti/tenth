package com.example.tenthskate.Clients.ModelClass;

public class Upload {

    private String productName,quantity,uploadId;
    private String productCode;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityy() {
        return quantityy;
    }

    public void setQuantityy(String quantityy) {
        this.quantityy = quantityy;
    }

    private String productDescription,quantityy;
    private String productImageUrl;
    private String productCategory;
    private String productPrice;
    private String productQuantity="1";
    private String productID;
    private String searchableName;
    private  String productCuttedPrice;

    public Upload(){
    }

    public Upload(String productName, String productCode, String productDescription, String productImageUrl, String productCategory, String productPrice, String productQuantity, String productID, String searchableName, String productCuttedPrice, String quantity) {
        this.productName = productName;
        this.productCode = productCode;
        this.productDescription = productDescription;
        this.productImageUrl = productImageUrl;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productID = productID;
        this.searchableName = searchableName;
        this.productCuttedPrice = productCuttedPrice;
        this.quantity = quantity;
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

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
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

    public String getProductCuttedPrice() {
        return productCuttedPrice;
    }

    public void setProductCuttedPrice(String productCuttedPrice) {
        this.productCuttedPrice = productCuttedPrice;
    }
}
