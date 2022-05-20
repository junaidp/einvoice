package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class AccountingCustomerParty {
    @XmlElement(name = "cac:Party")
    private Party party;
    public AccountingCustomerParty(){}
    public AccountingCustomerParty(Party party) {
        this.party = party;
    }
}
