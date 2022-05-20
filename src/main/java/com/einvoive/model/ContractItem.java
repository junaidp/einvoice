package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ContractItem {
    @Id
    private String id;
    private String contractID;
    private String milestone;
    private String amount;
    private String milestoneDueDate;
    private String invoiceDate;
    private String invoiceDueDate;
    private String raiseInvoice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMilestoneDueDate() {
        return milestoneDueDate;
    }

    public void setMilestoneDueDate(String milestoneDueDate) {
        this.milestoneDueDate = milestoneDueDate;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceDueDate() {
        return invoiceDueDate;
    }

    public void setInvoiceDueDate(String invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    public String getRaiseInvoice() {
        return raiseInvoice;
    }

    public void setRaiseInvoice(String raiseInvoice) {
        this.raiseInvoice = raiseInvoice;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }
}
