package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class TaxSubtotal {
    @XmlElement(name = "cbc:TaxableAmount")
    private Amount taxableAmount;
    @XmlElement(name = "cbc:TaxAmount")
    private Amount taxAmount;
    @XmlElement(name = "cac:TaxCategory")
    private TaxCategory taxCategory;

    public TaxSubtotal() {
    }

    public TaxSubtotal(Amount taxableAmount, Amount taxAmount, TaxCategory taxCategory) {
        this.taxableAmount = taxableAmount;
        this.taxAmount = taxAmount;
        this.taxCategory = taxCategory;
    }
}
