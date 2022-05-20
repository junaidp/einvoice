package com.einvoive.zatcaxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class Attachment {
    @XmlElement(name = "cac:ExternalReference")
    private ExternalReference externalReference;
    public Attachment(){}
    public Attachment(ExternalReference externalReference) {
        this.externalReference = externalReference;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ExternalReference{
        @XmlElement(name = "cbc:URI")
        private String uri;
        public ExternalReference(){}
        public ExternalReference(String uri) {
            this.uri = uri;
        }
    }
}
