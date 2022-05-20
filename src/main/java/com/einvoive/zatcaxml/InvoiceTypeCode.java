package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cbc:InvoiceTypeCode")
public class InvoiceTypeCode {
    @XmlAttribute
    private String listID;
    @XmlAttribute
    private String listAgencyID;
    @XmlValue
    private String invoiceTypeCode;

    public InvoiceTypeCode(){}

    public InvoiceTypeCode(String listID, String listAgencyID, String invoiceTypeCode) {
        this.listID = listID;
        this.listAgencyID = listAgencyID;
        this.invoiceTypeCode = invoiceTypeCode;
    }
}
