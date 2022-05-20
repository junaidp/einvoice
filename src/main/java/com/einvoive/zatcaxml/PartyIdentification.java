package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "cac:PartyIdentification")
public class PartyIdentification {
    @XmlElement(name = "cbc:ID")
    private ID id;
    public PartyIdentification(){}
    public PartyIdentification(ID id) {
        this.id = id;
    }

}
