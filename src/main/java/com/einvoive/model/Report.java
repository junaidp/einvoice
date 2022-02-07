package com.einvoive.model;

public class Report {

    private String customerName;
    private String billToEnglish;
    private String customerVatNo;
    private String invoiceDate;
    private String invoiceNumber;
    private String totalExcludingVAT;
    private String totalVat;
    private String totalNetAmount;
    private String totalAmountDue;
    private String location;

    public String getBillToEnglish() {
        return billToEnglish;
    }

    public void setBillToEnglish(String billToEnglish) {
        this.billToEnglish = billToEnglish;
    }

    public String getTotalAmountDue() {
        return totalAmountDue;
    }

    public void setTotalAmountDue(String totalAmountDue) {
        this.totalAmountDue = totalAmountDue;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerVatNo() {
        return customerVatNo;
    }

    public void setCustomerVatNo(String customerVatNo) {
        this.customerVatNo = customerVatNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTotalExcludingVAT() {
        return totalExcludingVAT;
    }

    public void setTotalExcludingVAT(String totalExcludingVAT) {
        this.totalExcludingVAT = totalExcludingVAT;
    }

    public String getTotalVat() {
        return totalVat;
    }

    public void setTotalVat(String totalVat) {
        this.totalVat = totalVat;
    }

    public String getTotalNetAmount() {
        return totalNetAmount;
    }

    public void setTotalNetAmount(String totalNetAmount) {
        this.totalNetAmount = totalNetAmount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
