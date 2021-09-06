package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ProductMain {

    @Id
    private String id;
    private String productName;
    private String description;
    private String price;
    private String code;
    private String assignedChartofAccounts;
    private String userId;
    private String companyID;

    public ProductMain(String id, String productName, String description, String price, String code, String assignedChartofAccounts, String userId, String companyID) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.code = code;
        this.assignedChartofAccounts = assignedChartofAccounts;
        this.userId = userId;
        this.companyID = companyID;
    }

    public ProductMain() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAssignedChartofAccounts() {
        return assignedChartofAccounts;
    }

    public void setAssignedChartofAccounts(String assignedChartofAccounts) {
        this.assignedChartofAccounts = assignedChartofAccounts;
    }
}
