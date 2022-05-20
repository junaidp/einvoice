package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "cbc:InvoicedQuantity")
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoicedQuantity {
    @XmlAttribute
    private String unitCode;
    @XmlValue
    private String value;
    public InvoicedQuantity(){}
    public InvoicedQuantity(String unitCode, String value) {
        this.unitCode = unitCode;
        this.value = value;
    }
}
