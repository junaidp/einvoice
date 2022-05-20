package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class InvoicePeriod {
    @XmlElement(name = "cbc:StartDate")
    private String startDate;
    @XmlElement(name = "cbc:EndDate")
    private String endDate;
    public InvoicePeriod(){}
    public InvoicePeriod(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
