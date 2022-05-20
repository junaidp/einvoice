package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "cac:Country")
public class Country {
    @XmlElement(name = "cbc:IdentificationCode")
    private IdentificationCode identificationCode;
    public Country(){}
    public Country(IdentificationCode identificationCode) {
        this.identificationCode = identificationCode;
    }
}
