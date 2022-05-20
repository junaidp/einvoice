package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "cbc:ID")
public class Scheme {
    @XmlAttribute
    private String schemeID;
    @XmlAttribute
    private String schemeAgencyID;
    @XmlValue
    private String value;
    public Scheme(){}
    public Scheme(String schemeID, String schemeAgencyID, String value) {
        this.schemeID = schemeID;
        this.schemeAgencyID = schemeAgencyID;
        this.value = value;
    }
}
