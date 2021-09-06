package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Vat {

    @Id
    private String id;
    private String vatRates;
    private String companyID;

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVatRates() {
        return vatRates;
    }

    public void setVatRates(String vatRates) {
        this.vatRates = vatRates;
    }

}
