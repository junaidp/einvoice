package com.einvoive.zatcaxml;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class IdentificationCode{
    @XmlAttribute
    private String listID;
    @XmlAttribute
    private String listAgencyID;
    @XmlValue
    private String value;
    public IdentificationCode(){}
    public IdentificationCode(String listID, String listAgencyID, String value) {
        this.listID = listID;
        this.listAgencyID = listAgencyID;
        this.value = value;
    }

    public IdentificationCode(String value) {
        this.value = value;
    }
}
