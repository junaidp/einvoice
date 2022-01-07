package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class JournalEntries {
@Id
    private String id;
    private String entryState;
    private Date entryDate;
    private String customerName;
    private String invoiceNo;
    private String chartOfAccount;
    private String credit = "0";
    private String debit = "0";

    public void setEntryState(String entryState) {
        this.entryState = entryState;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public void setChartOfAccount(String chartOfAccount) {
        this.chartOfAccount = chartOfAccount;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getEntryState() {
        return entryState;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public String getChartOfAccount() {
        return chartOfAccount;
    }

    public String getCredit() {
        return credit;
    }

    public String getDebit() {
        return debit;
    }
}
