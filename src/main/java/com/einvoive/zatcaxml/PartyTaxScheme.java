package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cac:PartyTaxScheme")
public class PartyTaxScheme {
    @XmlElement(name = "cbc:CompanyID")
    private CompanyXML company;
    @XmlElement(name = "cac:TaxScheme")
    private TaxScheme taxScheme;
    public PartyTaxScheme(){}
    public PartyTaxScheme(CompanyXML company, TaxScheme taxScheme) {
        this.company = company;
        this.taxScheme = taxScheme;
    }
}
