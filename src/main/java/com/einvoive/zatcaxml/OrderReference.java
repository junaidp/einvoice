package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class OrderReference {
    @XmlElement(name = "cbc:ID")
    private String id;
    public OrderReference(){}
    public OrderReference(String id) {
        this.id = id;
    }
}
