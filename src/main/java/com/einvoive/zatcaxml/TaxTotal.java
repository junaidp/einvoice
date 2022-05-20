package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class TaxTotal {
    @XmlElement(name = "cbc:TaxAmount")
    private Amount amount;
    @XmlElement(name = "cac:TaxSubtotal")
    private TaxSubtotal TaxSubtotal;

    public TaxTotal() {
    }

    public TaxTotal(Amount amount, com.einvoive.zatcaxml.TaxSubtotal taxSubtotal) {
        this.amount = amount;
        TaxSubtotal = taxSubtotal;
    }
}
