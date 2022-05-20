package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cac:TaxScheme")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxScheme{
    @XmlElement(name = "cbc:ID")
    private Scheme scheme;
    public TaxScheme(){}
    public TaxScheme(Scheme scheme) {
        this.scheme = scheme;
    }
}
