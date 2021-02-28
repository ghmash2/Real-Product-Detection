package com.example.goods_ledger.Assets;

public class Product {

    private String productKey, productOwnerAccountID, productManufacturerID, productManufacturerName, productFactoryID, productID,
            productName, productType, productBatch, productSerialinBatch, productManufacturingLocation, productManufacturingDate, productExpiryDate;

    public Product(String productKey, String productOwnerAccountID, String productManufacturerID, String productManufacturerName, String productFactoryID, String productID, String productName, String productType, String productBatch, String productSerialinBatch, String productManufacturingLocation, String productManufacturingDate, String productExpiryDate) {
        this.productKey = productKey;
        this.productOwnerAccountID = productOwnerAccountID;
        this.productManufacturerID = productManufacturerID;
        this.productManufacturerName = productManufacturerName;
        this.productFactoryID = productFactoryID;
        this.productID = productID;
        this.productName = productName;
        this.productType = productType;
        this.productBatch = productBatch;
        this.productSerialinBatch = productSerialinBatch;
        this.productManufacturingLocation = productManufacturingLocation;
        this.productManufacturingDate = productManufacturingDate;
        this.productExpiryDate = productExpiryDate;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductOwnerAccountID() {
        return productOwnerAccountID;
    }

    public void setProductOwnerAccountID(String productOwnerAccountID) {
        this.productOwnerAccountID = productOwnerAccountID;
    }

    public String getProductManufacturerID() {
        return productManufacturerID;
    }

    public void setProductManufacturerID(String productManufacturerID) {
        this.productManufacturerID = productManufacturerID;
    }

    public String getProductManufacturerName() {
        return productManufacturerName;
    }

    public void setProductManufacturerName(String productManufacturerName) {
        this.productManufacturerName = productManufacturerName;
    }

    public String getProductFactoryID() {
        return productFactoryID;
    }

    public void setProductFactoryID(String productFactoryID) {
        this.productFactoryID = productFactoryID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductBatch() {
        return productBatch;
    }

    public void setProductBatch(String productBatch) {
        this.productBatch = productBatch;
    }

    public String getProductSerialinBatch() {
        return productSerialinBatch;
    }

    public void setProductSerialinBatch(String productSerialinBatch) {
        this.productSerialinBatch = productSerialinBatch;
    }

    public String getProductManufacturingLocation() {
        return productManufacturingLocation;
    }

    public void setProductManufacturingLocation(String productManufacturingLocation) {
        this.productManufacturingLocation = productManufacturingLocation;
    }

    public String getProductManufacturingDate() {
        return productManufacturingDate;
    }

    public void setProductManufacturingDate(String productManufacturingDate) {
        this.productManufacturingDate = productManufacturingDate;
    }

    public String getProductExpiryDate() {
        return productExpiryDate;
    }

    public void setProductExpiryDate(String productExpiryDate) {
        this.productExpiryDate = productExpiryDate;
    }
}
