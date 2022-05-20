package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlElement;

public class AdditionalDocumentReference {
    @XmlElement(name = "cbc:ID")
    private String ID;
    @XmlElement(name = "cbc:DocumentType")
    private String documentType;
    @XmlElement(name = "cac:Attachment")
    private Attachment attachment;
    public AdditionalDocumentReference(){}
    public AdditionalDocumentReference(String ID, String documentType, Attachment attachment) {
        this.ID = ID;
        this.documentType = documentType;
        this.attachment = attachment;
    }
}
