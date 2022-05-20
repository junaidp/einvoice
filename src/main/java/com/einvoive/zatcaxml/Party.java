package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cac:Party")
public class Party {
    @XmlElement(name = "cbc:EndpointID")
    private EndpointID endpointID;
    @XmlElement(name = "cac:PartyIdentification")
    private PartyIdentification partyIdentification;
    @XmlElement(name = "cac:PartyName")
    private PartyName partyName;
    @XmlElement(name = "cac:PostalAddress")
    private PostalAddress postalAddress;
    @XmlElement(name = "cac:PartyTaxScheme")
    private PartyTaxScheme taxScheme;
    @XmlElement(name = "cac:PartyLegalEntity")
    private PartyLegalEntity partyLegalEntity;
    @XmlElement(name = "cac:Contact")
    private Contact contact;
    @XmlElement(name = "cac:Person")
    private Person person;
    public Party(){}
    public Party(EndpointID endpointID, PartyIdentification partyIdentification, PartyName partyName, PostalAddress postalAddress, PartyTaxScheme taxScheme, PartyLegalEntity partyLegalEntity, Contact contact, Person person) {
        this.endpointID = endpointID;
        this.partyIdentification = partyIdentification;
        this.partyName = partyName;
        this.postalAddress = postalAddress;
        this.taxScheme = taxScheme;
        this.partyLegalEntity = partyLegalEntity;
        this.contact = contact;
        this.person = person;
    }

    public static class PartyName{
        @XmlElement(name = "cbc:Name")
        private String name;
        public PartyName(){}
        public PartyName(String name) {
            this.name = name;
        }
    }
}
