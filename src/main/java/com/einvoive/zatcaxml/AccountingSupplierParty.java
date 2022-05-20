package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class AccountingSupplierParty {
    @XmlElement(name = "cac:Party")
    private Party party;
    public AccountingSupplierParty(){}
    public AccountingSupplierParty(Party party) {
        this.party = party;
    }
}
