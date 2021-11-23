package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class InvoiceQR {
    @Id
    private String id;
    private String vatNumberCompany;
    private String companyName;
    private String exclusiveVAT;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVatNumberCompany() {
        return vatNumberCompany;
    }

    public void setVatNumberCompany(String vatNumberCompany) {
        this.vatNumberCompany = vatNumberCompany;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getExclusiveVAT() {
        return exclusiveVAT;
    }

    public void setExclusiveVAT(String exclusiveVAT) {
        this.exclusiveVAT = exclusiveVAT;
    }
}
