package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class PaymentTerms {
    @XmlElement(name = "cbc:Note")
    private String note;
    public PaymentTerms(){}
    public PaymentTerms(String note) {
        this.note = note;
    }
}
