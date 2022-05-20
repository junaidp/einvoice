package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class CompanyXML{
    @XmlAttribute
    private String schemeAgencyID;
    @XmlAttribute
    private String schemeID;
    @XmlValue
    private String value;

    public CompanyXML() {
    }

    public CompanyXML(String schemeAgencyID, String schemeID, String value) {
        this.schemeAgencyID = schemeAgencyID;
        this.schemeID = schemeID;
        this.value = value;
    }
}
