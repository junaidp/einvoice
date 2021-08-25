package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Vat {

    @Id
    private String id;

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

    private String vatRates;
}
