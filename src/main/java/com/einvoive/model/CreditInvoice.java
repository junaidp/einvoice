package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Document
public class CreditInvoice {
    @Id
    private String id;
    private String invoiceNumber;
    private String serialNo;
    private String hash;
    private String previousHash;
    private String paymentType;
    private String originalRefNo;
    private String type;
    private String supplyDate;
    private String dateTime;
    private String reasonChangingStandard;
    private String invoiceName;
    private String invoiceDescription;
    private String posoNumber;
    private String billTo;
    private String invoiceDate;
    private String paymentDue;
    private String total;
    private String discount;
    private String totalTaxableAmount;
    private String totalVat;
    private String totalAmountDue;
    private String userId;
    private String notes;
    private String status;
    private String currency;
    private String companyID;
    private String customerName;
    private String totalExcludingVAT;
    private String serviceLocation;
    private String downPayment;
    private String retention;
    private String totalNetAmount;
    private String recordPayment;
    private String bankAccount;
    private String referenceField;
    private String projectCode;
    private String location;
    private List<LineItem> lineItemList;
    private String amountSAR;

    public String getAmountSAR() {
        return amountSAR;
    }

    public void setAmountSAR(String amountSAR) {
        this.amountSAR = amountSAR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOriginalRefNo() {
        return originalRefNo;
    }

    public void setOriginalRefNo(String originalRefNo) {
        this.originalRefNo = originalRefNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

       public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getReasonChangingStandard() {
        return reasonChangingStandard;
    }

    public void setReasonChangingStandard(String reasonChangingStandard) {
        this.reasonChangingStandard = reasonChangingStandard;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }

    public String getPosoNumber() {
        return posoNumber;
    }

    public void setPosoNumber(String posoNumber) {
        this.posoNumber = posoNumber;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getSupplyDate() {
        return supplyDate;
    }

    public void setSupplyDate(String supplyDate) {
        this.supplyDate = supplyDate;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getPaymentDue() {
        return paymentDue;
    }

    public void setPaymentDue(String paymentDue) {
        this.paymentDue = paymentDue;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalTaxableAmount() {
        return totalTaxableAmount;
    }

    public void setTotalTaxableAmount(String totalTaxableAmount) {
        this.totalTaxableAmount = totalTaxableAmount;
    }

    public String getTotalVat() {
        return totalVat;
    }

    public void setTotalVat(String totalVat) {
        this.totalVat = totalVat;
    }

    public String getTotalAmountDue() {
        return totalAmountDue;
    }

    public void setTotalAmountDue(String totalAmountDue) {
        this.totalAmountDue = totalAmountDue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTotalExcludingVAT() {
        return totalExcludingVAT;
    }

    public void setTotalExcludingVAT(String totalExcludingVAT) {
        this.totalExcludingVAT = totalExcludingVAT;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public String getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(String downPayment) {
        this.downPayment = downPayment;
    }

    public String getRetention() {
        return retention;
    }

    public void setRetention(String retention) {
        this.retention = retention;
    }

    public String getTotalNetAmount() {
        return totalNetAmount;
    }

    public void setTotalNetAmount(String totalNetAmount) {
        this.totalNetAmount = totalNetAmount;
    }

    public String getRecordPayment() {
        return recordPayment;
    }

    public void setRecordPayment(String recordPayment) {
        this.recordPayment = recordPayment;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getReferenceField() {
        return referenceField;
    }

    public void setReferenceField(String referenceField) {
        this.referenceField = referenceField;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<LineItem> getLineItemList() {
        return lineItemList;
    }

    public void setLineItemList(List<LineItem> lineItemList) {
        this.lineItemList = lineItemList;
    }
}