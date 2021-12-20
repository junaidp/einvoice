package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class InvoiceB2C {

    @Id
    private String id;
    private String serialNo;
    private String hash;
    private String previousHash;
    private String type;
    private String reasonChangingStandard;
    private String invoiceNumber;
    private String invoiceName;
    private String billToEnglish;
    private Date invoiceDate;
    private String total;
    private String discount;
    private String totalTaxableAmount;
    private String totalVat;
    private String totalAmountDue;
    private String userId;
    private String notes;
    private String dateTime;
    private String companyID;
    private String billToArabicName;
    private String otherBuyerid;
    private String totalExcludingVAT;
    private String originalRefNo;
    private List<LineItemB2C> lineItemList;

    public String getId() {
        return id;
    }

    public String setId(String id) {
        this.id = id;
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }
    public String getOriginalRefNo() {
        return originalRefNo;
    }

    public void setOriginalRefNo(String originalRefNo) {
        this.originalRefNo = originalRefNo;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBillToEnglish() {
        return billToEnglish;
    }

    public void setBillToEnglish(String billToEnglish) {
        this.billToEnglish = billToEnglish;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getBillToArabicName() {
        return billToArabicName;
    }

    public void setBillToArabicName(String billToArabicName) {
        this.billToArabicName = billToArabicName;
    }

    public String getOtherBuyerid() {
        return otherBuyerid;
    }

    public void setOtherBuyerid(String otherBuyerid) {
        this.otherBuyerid = otherBuyerid;
    }

    public String getTotalExcludingVAT() {
        return totalExcludingVAT;
    }

    public void setTotalExcludingVAT(String totalExcludingVAT) {
        this.totalExcludingVAT = totalExcludingVAT;
    }

    public List<LineItemB2C> getLineItemList() {
        return lineItemList;
    }

    public void setLineItemList(List<LineItemB2C> lineItemList) {
        this.lineItemList = lineItemList;
    }

}