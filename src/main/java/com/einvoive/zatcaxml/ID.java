package com.einvoive.zatcaxml;


import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cbc:ID")
public class ID{
    @XmlAttribute
    private String schemeID;
    @XmlAttribute
    private String schemeAgencyID;
    @XmlValue
    private String value;
    public ID(){}
    public ID(String schemeID, String value) {
        this.schemeID = schemeID;
        this.value = value;
    }

    public ID(String schemeID, String schemeAgencyID, String value) {
        this.schemeID = schemeID;
        this.schemeAgencyID = schemeAgencyID;
        this.value = value;
    }
}
