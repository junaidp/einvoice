package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PayeeParty {
    @XmlElement(name = "cac:PartyIdentification")
    private PartyIdentification partyIdentification;
    @XmlElement(name = "cac:PartyName")
    private Party.PartyName partyName;
    @XmlElement(name = "cac:PartyLegalEntity")
    private PartyLegalEntity partyLegalEntity;
    public PayeeParty(){}
    public PayeeParty(PartyIdentification partyIdentification, Party.PartyName partyName, PartyLegalEntity partyLegalEntity) {
        this.partyIdentification = partyIdentification;
        this.partyName = partyName;
        this.partyLegalEntity = partyLegalEntity;
    }
}
