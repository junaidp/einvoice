package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "cbc:EndpointID")
public class EndpointID {
    @XmlAttribute
    private String schemeID;
    @XmlAttribute
    private String schemeAgencyID;
    @XmlValue
    private String value;
    public EndpointID(){}
    public EndpointID(String schemeID, String schemeAgencyID, String value) {
        this.schemeID = schemeID;
        this.schemeAgencyID = schemeAgencyID;
        this.value = value;
    }
    public EndpointID(String value) {
        this.value = value;
    }
}
