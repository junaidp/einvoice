package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cac:Contact")
public class Contact {
    @XmlElement(name = "cbc:Telephone")
    private String telephone;
    @XmlElement(name = "cbc:Telefax")
    private String telefax;
    @XmlElement(name = "cbc:ElectronicMail")
    private String electronicMail;
    public Contact(){}
    public Contact(String telephone, String telefax, String electronicMail) {
        this.telephone = telephone;
        this.telefax = telefax;
        this.electronicMail = electronicMail;
    }
}
