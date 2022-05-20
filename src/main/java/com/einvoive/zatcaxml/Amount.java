package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Amount {
    @XmlAttribute
    private String currencyID;
    @XmlValue
    private String value;
    public Amount(){}
    public Amount(String currencyID, String value) {
        this.currencyID = currencyID;
        this.value = value;
    }
}
