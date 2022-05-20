package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Contract {
    @Id
    private String id;
    private String companyID;
    private String projectName;
    private String projectLocation;
    private String projectDescription;
    private String projectManager;
    private String customerName;
    private String projectType;
    private String projectTotalFee;
    private String contractCurrency;
    private String retentionMoney;
    private String addExtracKey;
    private String paymentTerms;
    private String customerId;
    private String textField;
    private List<ContractItem> contractItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }
    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectTotalFee() {
        return projectTotalFee;
    }

    public void setProjectTotalFee(String projectTotalFee) {
        this.projectTotalFee = projectTotalFee;
    }

    public String getContractCurrency() {
        return contractCurrency;
    }

    public void setContractCurrency(String contractCurrency) {
        this.contractCurrency = contractCurrency;
    }

    public String getRetentionMoney() {
        return retentionMoney;
    }

    public void setRetentionMoney(String retentionMoney) {
        this.retentionMoney = retentionMoney;
    }

    public String getAddExtracKey() {
        return addExtracKey;
    }

    public void setAddExtracKey(String addExtracKey) {
        this.addExtracKey = addExtracKey;
    }

    public List<ContractItem> getContractItems() {
        return contractItems;
    }

    public void setContractItems(List<ContractItem> contractItems) {
        this.contractItems = contractItems;
    }
}
