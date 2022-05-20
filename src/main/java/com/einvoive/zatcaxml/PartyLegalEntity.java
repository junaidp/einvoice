package com.einvoive.zatcaxml;

import com.einvoive.model.Company;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cac:PartyLegalEntity")
public class PartyLegalEntity {
    @XmlElement(name = "cbc:RegistrationName")
    private String registrationName;
    @XmlElement(name = "cbc:CompanyID")
    private CompanyXML company;
    @XmlElement(name = "cac:RegistrationAddress")
    private RegistrationAddress registrationAddress;
    public PartyLegalEntity(){}
    public PartyLegalEntity(String registrationName, CompanyXML company, RegistrationAddress registrationAddress) {
        this.registrationName = registrationName;
        this.company = company;
        this.registrationAddress = registrationAddress;
    }

    public void setCompany(CompanyXML company) {
        this.company = company;
    }
}
