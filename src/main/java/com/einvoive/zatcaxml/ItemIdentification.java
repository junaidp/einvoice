package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ItemIdentification {
    @XmlElement(name = "cbc:ID")
    private EndpointID id;
    public ItemIdentification(){}
    public ItemIdentification(EndpointID id) {
        this.id = id;
    }
}
