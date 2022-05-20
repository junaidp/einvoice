package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class ContractDocumentReference {
    @XmlElement(name = "cbc:ID")
    private String id;
    @XmlElement(name = "cbc:DocumentType")
    private String documentType;
    public ContractDocumentReference(){}
    public ContractDocumentReference(String id, String documentType) {
        this.id = id;
        this.documentType = documentType;
    }
}
